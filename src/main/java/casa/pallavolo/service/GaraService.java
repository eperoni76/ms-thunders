package casa.pallavolo.service;

import casa.pallavolo.dto.GaraDTO;
import casa.pallavolo.dto.GiocatoreDTO;
import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.model.AddettoDefibrillatore;
import casa.pallavolo.model.Dirigente;
import casa.pallavolo.model.Gara;
import casa.pallavolo.model.Squadra;
import casa.pallavolo.repository.GaraRepository;
import casa.pallavolo.repository.SquadraRepository;
import casa.pallavolo.utils.Constants;
import casa.pallavolo.utils.Paths;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

@Service
public class GaraService {
    @Autowired
    private GaraRepository garaRepository;
    @Autowired
    private SquadraService squadraService;
    @Autowired
    private GiocatoreService giocatoreService;
    @Autowired
    private AddettoDefibrillatoreService addettoDefibrillatoreService;
    @Autowired
    private DirigenteService dirigenteService;
    @Autowired
    private ModelMapper garaMapper;
    @Autowired
    private EntityManager entityManager;

    private boolean calcolaTrasferta(GaraDTO gara){
        return !gara.getOspitante().toLowerCase().contains(gara.getSquadra().getNomeSquadra().toLowerCase());
    }

    private Integer trovaIdSquadra(Gara gara) {
        String ospitante = gara.getOspitante().toLowerCase();
        String ospite = gara.getOspite().toLowerCase();
        List<SquadraDTO> squadre = squadraService.getAllSquadre();

        for (SquadraDTO squadra : squadre) {
            String nomeSquadra = squadra.getNomeSquadra().toLowerCase();
            if (ospitante.contains(nomeSquadra) || ospite.contains(nomeSquadra)) {
                return garaMapper.map(squadra, Squadra.class).getId();
            }
        }
        return null;
    }

    private LocalDate formattaData(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d/M/yyyy", Locale.ITALIAN);
        try {
            int annoCorrente = LocalDate.now().getYear();
            LocalDate date = LocalDate.parse(dateStr.toLowerCase()+"/"+annoCorrente, formatter);
            return date.withYear(LocalDate.now().getYear());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato data non valido: " + dateStr, e);
        }
    }

    public Gara inserisciGara(GaraDTO garaDaInserire){
        Gara gara = garaMapper.map(garaDaInserire, Gara.class);
        Squadra squadra = garaMapper.map(squadraService.getSquadraById(garaDaInserire.getSquadra().getId()), Squadra.class);
        gara.setSquadra(squadra);
        gara.setIsTrasferta(calcolaTrasferta(garaDaInserire));
        return garaRepository.save(gara);
    }

    public List<GaraDTO> getAllGare() {
        return garaRepository
                .findAll()
                .stream()
                .filter(gara -> gara.getRisultato() == null || gara.getRisultato().equalsIgnoreCase(""))
                .map(gara -> garaMapper.map(gara, GaraDTO.class))
                .sorted(Comparator.comparing(GaraDTO::getData))
                .toList();
    }

    public List<GaraDTO> getGareBySquadra(Integer idSquadra) {
        return garaRepository
                .findByIdSquadra(idSquadra)
                .stream()
                .filter(gara -> gara.getRisultato() == null || gara.getRisultato().equalsIgnoreCase(""))
                .map(gara -> garaMapper.map(gara, GaraDTO.class))
                .sorted(Comparator.comparing(GaraDTO::getData))
                .toList();
    }

    public List<GaraDTO> getGareConcluse(Integer idSquadra, Integer anno) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Gara> query = cb.createQuery(Gara.class);
        Root<Gara> root = query.from(Gara.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.isNotNull(root.get("risultato")));
        predicates.add(cb.notEqual(root.get("risultato"), ""));

        if (Objects.nonNull(idSquadra)) {
            predicates.add(cb.equal(root.get("squadra").get("id"), idSquadra));
        }

        if (Objects.nonNull(anno)) {
            predicates.add(cb.equal(cb.function("DATE_PART", Integer.class, cb.literal("year"), root.get("data")), anno));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("data")));

        return entityManager.createQuery(query)
                .getResultList()
                .stream()
                .map(gara -> garaMapper.map(gara, GaraDTO.class))
                .toList();
    }

    public GaraDTO modificaGara(GaraDTO garaDaModificare){
        Gara entity = garaRepository.findById(garaDaModificare.getId()).orElseThrow(() -> new EntityNotFoundException("Gara non presente"));
        Squadra squadra = garaMapper.map(squadraService.getSquadraById(garaDaModificare.getSquadra().getId()), Squadra.class);
        entity.setId(garaDaModificare.getId());
        entity.setSquadra(squadra);
        entity.setNumeroGara(garaDaModificare.getNumeroGara());
        entity.setData(garaDaModificare.getData());
        entity.setOra(garaDaModificare.getOra());
        entity.setIndirizzo(garaDaModificare.getIndirizzo());
        entity.setCampionato(garaDaModificare.getCampionato());
        entity.setOspitante(garaDaModificare.getOspitante());
        entity.setOspite(garaDaModificare.getOspite());
        entity.setIsTrasferta(garaDaModificare.getIsTrasferta());
        entity.setRisultato(garaDaModificare.getRisultato());
        entity.setIsVittoria(garaDaModificare.getIsVittoria());

        Gara garaAggiornata = garaRepository.save(entity);
        return garaMapper.map(garaAggiornata, GaraDTO.class);
    }

    public void eliminaGaraById(Integer id){
        garaRepository.deleteById(id);
    }

    public List<Gara> caricaCalendarioFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String fileName = file.getOriginalFilename();
            System.out.println("File ricevuto: " + fileName);
            List<Gara> gareDaInserire = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.equalsIgnoreCase("")){

                    String[] parts = line.split("///");
                    Gara gara = new Gara();
                    gara.setNumeroGara(Integer.parseInt(parts[0]));
                    gara.setData(formattaData(parts[1]));
                    gara.setOra(LocalTime.parse(parts[2]));
                    gara.setCampionato(parts[3]);
                    gara.setIndirizzo(parts[4]);
                    gara.setOspitante(parts[5]);
                    gara.setOspite(parts[6]);
                    Integer idSquadra = trovaIdSquadra(gara);
                    Squadra squadra = squadraService.getSquadraById(idSquadra);
                    if(Objects.nonNull(squadra)){
                        gara.setSquadra(squadra);
                        gara.setIsTrasferta(calcolaTrasferta(garaMapper.map(gara, GaraDTO.class)));
                    }
                    gareDaInserire.add(gara);
                }
            }
            for (Gara gara : gareDaInserire) {
                System.out.println(gara);
            }
            return garaRepository.saveAll(gareDaInserire);
        }
    }

    public Integer countVittorieBySquadra(Squadra squadra){
        return garaRepository.countVittorieBySquadra(squadra);
    }

    public Integer countSconfitteBySquadra(Squadra squadra){
        return garaRepository.countSconfitteBySquadra(squadra);
    }

    public byte[] condividiCalendario(List<GaraDTO> calendario) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titolo = new Paragraph("Calendario Gare", titleFont);
            titolo.setAlignment(Element.ALIGN_CENTER);
            titolo.setSpacingAfter(20);
            document.add(titolo);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            addTableHeader(table, "Data");
            addTableHeader(table, "Ora");
            addTableHeader(table, "Squadra Casa");
            addTableHeader(table, "Squadra Ospite");
            addTableHeader(table, "Indirizzo");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            for (GaraDTO gara : calendario) {
                String dataFormattata = gara.getData().format(formatter);

                table.addCell(dataFormattata);
                table.addCell(gara.getOra().toString());
                table.addCell(gara.getOspitante());
                table.addCell(gara.getOspite());
                table.addCell(gara.getIndirizzo());
            }

            document.add(table);
        } catch (DocumentException e) {
            throw new IOException("Errore nella generazione del PDF: " + e.getMessage());
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }



    private void addTableHeader(PdfPTable table, String header) {
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(5);
        table.addCell(headerCell);
    }

    public byte[] generaListaGara(GaraDTO datiGara) throws IOException {
        /**
         * APERTURA DOCUMENTO
         */
        ByteArrayOutputStream risultato = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, risultato);
        document.open();
        Color darkGreen = new Color(0, 128, 0);
        Color darkRed = new Color(139, 0, 0);
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter oraFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dataNascitaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        /**
         * CARICAMENTO IMMAGINI
         */
        Image image1 = com.lowagie.text.Image.getInstance(Paths.UISP_IMAGE_1);
        Image image2 = com.lowagie.text.Image.getInstance(Paths.UISP_IMAGE_2);
        Image image3 = com.lowagie.text.Image.getInstance(Paths.UISP_IMAGE_3);

        image1.scaleToFit(100, 100);
        image2.scaleToFit(100, 100);
        image3.scaleToFit(100, 100);

        PdfPTable imageTable = new PdfPTable(3);
        imageTable.setWidthPercentage(100);

        PdfPCell cellImage = new PdfPCell(image1);
        cellImage.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellImage.setBorder(0);
        imageTable.addCell(cellImage);

        PdfPCell cellImage2 = new PdfPCell(image2);
        cellImage2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellImage2.setBorder(0);
        imageTable.addCell(cellImage2);

        PdfPCell cellImage3 = new PdfPCell(image3);
        cellImage3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellImage3.setBorder(0);
        imageTable.addCell(cellImage3);
        imageTable.setSpacingAfter(20f);
        document.add(imageTable);


        /**
         * TITOLI
         */
        PdfPTable title1 = new PdfPTable(1);
        title1.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell(new Phrase(Constants.TITOLO_PDF, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.NORMAL, darkGreen)));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        title1.addCell(cell1);
        title1.setSpacingAfter(10f);
        document.add(title1);


        PdfPTable title2 = new PdfPTable(1);
        title2.setWidthPercentage(100);
        PdfPCell cell2 = new PdfPCell(new Phrase(Constants.PRIMO_SOTTOTITOLO, new com.lowagie.text.Font(Font.HELVETICA, 11, Font.BOLD, darkGreen)));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(Rectangle.BOX);
        cell2.setPadding(5f);
        title2.addCell(cell2);
        title2.setSpacingAfter(10f);
        document.add(title2);


        Squadra squadraSelezionata = squadraService.getSquadraById(datiGara.getSquadra().getId());
        PdfPTable title3 = new PdfPTable(1);
        title3.setWidthPercentage(100);
        Chunk chunkSquadra1 = new Chunk(Constants.SECONDO_SOTTOTITOLO, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunkSquadra2 = new Chunk("          " + squadraSelezionata.getNomeUfficiale(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, Color.BLACK));
        Phrase squadra = new Phrase();
        squadra.add(chunkSquadra1);
        squadra.add(chunkSquadra2);
        PdfPCell cell3 = new PdfPCell(squadra);

        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setBorder(com.lowagie.text.Rectangle.BOX);
        cell3.setPadding(5f);
        title3.addCell(cell3);
        title3.setSpacingAfter(10f);
        document.add(title3);

        /**
         * DATI DELLA GARA
         */
        PdfPTable gara1 = new PdfPTable(3);
        gara1.setWidthPercentage(100);
        gara1.setWidths(new float[]{0.2f, 0.4f, 0.4f});

        Chunk chunk1_1 = new Chunk("N^ GARA", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk1_2 = new Chunk("    " + datiGara.getNumeroGara(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK));
        PdfPCell garaCell1 = new PdfPCell();
        Phrase phrase1 = new Phrase();
        phrase1.add(chunk1_1);
        phrase1.add(chunk1_2);
        garaCell1.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell1.setPadding(2f);
        garaCell1.addElement(phrase1);
        gara1.addCell(garaCell1);

        Chunk chunk2_1 = new Chunk("OSPITANTE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk2_2 = new Chunk("    " + datiGara.getOspitante(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK));
        PdfPCell garaCell2 = new PdfPCell();
        Phrase phrase2 = new Phrase();
        phrase2.add(chunk2_1);
        phrase2.add(chunk2_2);
        garaCell2.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell2.setPadding(2f);
        garaCell2.addElement(phrase2);
        gara1.addCell(garaCell2);

        Chunk chunk3_1 = new Chunk("OSPITE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk3_2 = new Chunk("    " + datiGara.getOspite(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK));
        PdfPCell garaCell3 = new PdfPCell();
        Phrase phrase3 = new Phrase();
        phrase3.add(chunk3_1);
        phrase3.add(chunk3_2);
        garaCell3.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell3.setPadding(2f);
        garaCell3.addElement(phrase3);
        gara1.addCell(garaCell3);
        document.add(gara1);

        PdfPTable gara2 = new PdfPTable(2);
        gara2.setWidthPercentage(100);
        gara2.setWidths(new float[]{0.7f, 0.3f});

        Chunk chunk4_1 = new Chunk("INDIRIZZO", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk4_2 = new Chunk("     "+datiGara.getIndirizzo(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK));
        PdfPCell garaCell4 = new PdfPCell();
        Phrase phrase4 = new Phrase();
        phrase4.add(chunk4_1);
        phrase4.add(chunk4_2);
        garaCell4.setPadding(2f);
        garaCell4.addElement(phrase4);
        gara2.addCell(garaCell4);


        Chunk chunk6_1 = new Chunk("DATA E ORA", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk6_2 = new Chunk("    " + dataFormatter.format(datiGara.getData()) + "   " + oraFormatter.format(datiGara.getOra()), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell6 = new PdfPCell();
        Phrase phrase6 = new Phrase();
        phrase6.add(chunk6_1);
        phrase6.add(chunk6_2);
        garaCell6.setPadding(2f);
        garaCell6.addElement(phrase6);
        gara2.addCell(garaCell6);
        document.add(gara2);

        PdfPTable gara3 = new PdfPTable(1);
        gara3.setWidthPercentage(100);

        Chunk chunk7_1 = new Chunk("CAMPIONATO", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen));
        Chunk chunk7_2 = new Chunk("    " + datiGara.getCampionato(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK));
        PdfPCell garaCell7 = new PdfPCell();
        Phrase phrase7 = new Phrase();
        phrase7.add(chunk7_1);
        phrase7.add(chunk7_2);
        garaCell7.setPadding(2f);
        garaCell7.addElement(phrase7);
        gara3.addCell(garaCell7);
        gara3.setSpacingAfter(10f);
        document.add(gara3);

        /**
         * TABELLA GIOCATORI
         */
        List<GiocatoreDTO> giocatori = giocatoreService.getGiocatoriBySquadra(garaMapper.map(datiGara.getSquadra(), Squadra.class));
        PdfPTable tableGiocatori = new PdfPTable(5);
        tableGiocatori.setWidthPercentage(100);
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, darkGreen);
        com.lowagie.text.Font nomiFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK);
        com.lowagie.text.Font dataFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, Color.BLACK);
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("N^", titleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGiocatori.addCell(cell);

        cell = new PdfPCell(new Phrase("COGNOME E NOME", titleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGiocatori.addCell(cell);

        cell = new PdfPCell(new Phrase("DATA DI NASCITA", titleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGiocatori.addCell(cell);

        cell = new PdfPCell(new Phrase("TESSERA UISP", titleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGiocatori.addCell(cell);

        cell = new PdfPCell(new Phrase("DOCUMENTO", titleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGiocatori.addCell(cell);

        float[] columnWidths = {0.8f, 4f, 3f, 3f, 2f};
        tableGiocatori.setWidths(columnWidths);

        for (GiocatoreDTO giocatore : giocatori) {
            cell = new PdfPCell(new Phrase(giocatore.getNumeroMaglia().toString(), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableGiocatori.addCell(cell);

            PdfPCell nomeCell;
            if (giocatore.getRuolo().equalsIgnoreCase("libero") || giocatore.getIsCapitano()) {
                PdfPTable nestedTable = new PdfPTable(2);
                nestedTable.setWidthPercentage(100);
                nestedTable.setWidths(new float[]{90, 10});

                PdfPCell nomeCognomeCell = new PdfPCell(new Phrase(giocatore.getCognome().toUpperCase() + " " + giocatore.getNome().toUpperCase(), nomiFont));
                nomeCognomeCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
                nomeCognomeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nestedTable.addCell(nomeCognomeCell);

                String aggiunta = giocatore.getIsCapitano() ? "K" : "L";
                PdfPCell liberoCell = new PdfPCell(new Phrase(aggiunta, nomiFont));
                liberoCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
                liberoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nestedTable.addCell(liberoCell);

                nomeCell = new PdfPCell(nestedTable);
            } else {
                nomeCell = new PdfPCell(new Phrase(giocatore.getCognome().toUpperCase() + " " + giocatore.getNome().toUpperCase(), nomiFont));
            }
            nomeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nomeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableGiocatori.addCell(nomeCell);

            cell = new PdfPCell(new Phrase(dataNascitaFormatter.format(giocatore.getDataNascita()), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableGiocatori.addCell(cell);

            cell = new PdfPCell(new Phrase(giocatore.getTesseraUisp(), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableGiocatori.addCell(cell);

            cell = new PdfPCell(new Phrase("", dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableGiocatori.addCell(cell);
        }

        document.add(tableGiocatori);

        /**
         * TABELLA ALLENATORI E DIRIGENTI
         */
        PdfPTable tableDirigenti = new PdfPTable(4);
        tableDirigenti.setWidthPercentage(100);
        float[] columnWidthsDirigenti = {3f, 4.8f, 3f, 2f};
        tableDirigenti.setWidths(columnWidthsDirigenti);

        PdfPCell cell1Allenatore = new PdfPCell(new Phrase("1^ ALLENATORE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen)));
        cell1Allenatore.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1Allenatore.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(cell1Allenatore);

        StringBuilder primoAllenatoreNome = new StringBuilder();
        String primoAllenatoreTesseraUisp = "";
        if(datiGara.getAllenatore()!=null){
            Dirigente primoAllenatore = dirigenteService.getDirigenteById(datiGara.getAllenatore());
            primoAllenatoreNome.append(primoAllenatore.getCognome().toUpperCase()).append(" ").append(primoAllenatore.getNome().toUpperCase());
            primoAllenatoreTesseraUisp = primoAllenatore.getTesseraUisp();
        }
        PdfPCell allenatoreCellValue = new PdfPCell(new Phrase(primoAllenatoreNome.toString(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK)));
        allenatoreCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        allenatoreCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(allenatoreCellValue);

        PdfPCell allenatoreTesseraCellValue = new PdfPCell(new Phrase(primoAllenatoreTesseraUisp, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, Color.BLACK)));
        allenatoreTesseraCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        allenatoreTesseraCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(allenatoreTesseraCellValue);

        tableDirigenti.addCell("");

        PdfPCell cell2Allenatore = new PdfPCell(new Phrase("2^ ALLENATORE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen)));
        cell2Allenatore.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2Allenatore.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(cell2Allenatore);

        StringBuilder secondoAllenatoreNome = new StringBuilder();
        String secondoAllenatoreTesseraUisp = "";
        if(datiGara.getSecondoAllenatore()!=null){
            Dirigente secondoAllenatore = dirigenteService.getDirigenteById(datiGara.getSecondoAllenatore());
            secondoAllenatoreNome.append(secondoAllenatore.getCognome().toUpperCase()).append(" ").append(secondoAllenatore.getNome().toUpperCase());
            secondoAllenatoreTesseraUisp = secondoAllenatore.getTesseraUisp();
        }
        PdfPCell secondoAllenatoreCellValue = new PdfPCell(new Phrase(secondoAllenatoreNome.toString(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK)));
        secondoAllenatoreCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        secondoAllenatoreCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(secondoAllenatoreCellValue);

        PdfPCell secondoAllenatoreTesseraCellValue = new PdfPCell(new Phrase(secondoAllenatoreTesseraUisp, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, Color.BLACK)));
        secondoAllenatoreTesseraCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        secondoAllenatoreTesseraCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(secondoAllenatoreTesseraCellValue);

        tableDirigenti.addCell("");

        PdfPCell cellDirigente = new PdfPCell(new Phrase("DIRIGENTE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, darkGreen)));
        cellDirigente.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDirigente.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(cellDirigente);

        StringBuilder dirigenteNome = new StringBuilder();
        String dirigenteTesseraUisp = "";
        if(datiGara.getDirigente()!=null){
            Dirigente dirigente = dirigenteService.getDirigenteById(datiGara.getDirigente());
            dirigenteNome.append(dirigente.getCognome().toUpperCase()).append(" ").append(dirigente.getNome().toUpperCase());
            dirigenteTesseraUisp = dirigente.getTesseraUisp();
        }
        PdfPCell dirigenteCellValue = new PdfPCell(new Phrase(dirigenteNome.toString().toUpperCase(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.BOLD, Color.BLACK)));
        dirigenteCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        dirigenteCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(dirigenteCellValue);

        PdfPCell dirigenteTesseraCellValue = new PdfPCell(new Phrase(dirigenteTesseraUisp, new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, Color.BLACK)));
        dirigenteTesseraCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        dirigenteTesseraCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(dirigenteTesseraCellValue);

        tableDirigenti.addCell("");

        tableDirigenti.setSpacingAfter(10f);
        document.add(tableDirigenti);

        /**
         * TABELLA DEFIBRILLATORE
         */
        PdfPTable tableInformazioni = new PdfPTable(1);
        tableInformazioni.setWidthPercentage(100);

        com.lowagie.text.Font redBoldFont1 = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD, darkRed);
        com.lowagie.text.Font redBoldFont2 = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, darkRed);

        PdfPCell cellPrimaRiga = new PdfPCell(new Phrase(Constants.TITOLO_DEFIBRILLATORE, redBoldFont1));
        cellPrimaRiga.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPrimaRiga.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellPrimaRiga.setBorder(com.lowagie.text.Rectangle.BOX);
        cellPrimaRiga.setFixedHeight(35f);
        tableInformazioni.addCell(cellPrimaRiga);

        PdfPCell cellSecondaRiga = new PdfPCell(new Phrase(Constants.SOTTOTITOLO_DEFIBRILLATORE, redBoldFont2));
        cellSecondaRiga.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSecondaRiga.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellSecondaRiga.setBorder(com.lowagie.text.Rectangle.BOX);
        cellSecondaRiga.setFixedHeight(35f);
        tableInformazioni.addCell(cellSecondaRiga);
        tableInformazioni.setSpacingAfter(10f);
        document.add(tableInformazioni);

        /**
         * TABELLA DICHIARAZIONE DA FIRMARE
         */
        com.lowagie.text.Font defaultFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10,  com.lowagie.text.Font.BOLD, Color.BLACK);
        Phrase sottoscritto = new Phrase("IL/LA SOTTOSCRITTO/A: ", defaultFont);
        Phrase codiceFiscale = new Phrase("CODICE FISCALE: ", defaultFont);
        Phrase dataNascita = new Phrase("NATO/A IL: ", defaultFont);
        Phrase luogoNascita = new Phrase("A: ", defaultFont);
        Phrase luogoResidenza = new Phrase("RESIDENTE IN: ", defaultFont);

        if(datiGara.getAddettoDefibrillatore()!=null){
            AddettoDefibrillatore addettoDefibrillatore = addettoDefibrillatoreService.getAddettoDefibrillatoreById(datiGara.getAddettoDefibrillatore());
            sottoscritto.add(new Chunk(addettoDefibrillatore.getNome().toUpperCase() + " " + addettoDefibrillatore.getCognome().toUpperCase(), defaultFont));
            codiceFiscale.add(new Chunk(addettoDefibrillatore.getCodiceFiscale().toUpperCase(), defaultFont));
            dataNascita.add(new Chunk(dataNascitaFormatter.format(addettoDefibrillatore.getDataNascita()), defaultFont));
            luogoNascita.add(new Chunk(addettoDefibrillatore.getLuogoNascita(), defaultFont));
            luogoResidenza.add(new Chunk(addettoDefibrillatore.getLuogoResidenza(), defaultFont));
        }

        PdfPTable tablePrima = new PdfPTable(2);
        tablePrima.setWidthPercentage(100);

        PdfPCell cellSottoscritto = new PdfPCell(sottoscritto);
        cellSottoscritto.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tablePrima.addCell(cellSottoscritto);

        PdfPCell cellCodiceFiscale = new PdfPCell(codiceFiscale);
        cellCodiceFiscale.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tablePrima.addCell(cellCodiceFiscale);
        tablePrima.setSpacingAfter(5f);
        document.add(tablePrima);

        PdfPTable tableSeconda = new PdfPTable(3);
        tableSeconda.setWidthPercentage(100);
        tableSeconda.setWidths(new float[]{0.3f, 0.2f, 0.5f});

        PdfPCell cellNatoIl = new PdfPCell(dataNascita);
        cellNatoIl.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tableSeconda.addCell(cellNatoIl);

        PdfPCell cellA = new PdfPCell(luogoNascita);
        cellA.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tableSeconda.addCell(cellA);

        PdfPCell cellResidenteIn = new PdfPCell(luogoResidenza);
        cellResidenteIn.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tableSeconda.addCell(cellResidenteIn);
        tableSeconda.setSpacingAfter(5f);
        document.add(tableSeconda);

        PdfPTable tableTerza = new PdfPTable(1);
        tableTerza.setWidthPercentage(100);

        PdfPCell cellConsapevole = new PdfPCell(new Phrase(Constants.TESTO_DICHIARAZIONE_DEFIBRILLATORE, defaultFont));
        cellConsapevole.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cellConsapevole.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        tableTerza.addCell(cellConsapevole);
        tableTerza.setSpacingAfter(20f);
        document.add(tableTerza);

        /**
         * TABELLA DATA E FIRME
         */
        PdfPTable tabellaFirme = new PdfPTable(3);
        tabellaFirme.setWidthPercentage(100);

        PdfPCell cellData = new PdfPCell(new Paragraph("DATA_________________", defaultFont));
        cellData.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellData.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tabellaFirme.addCell(cellData);

        PdfPCell cellFirma = new PdfPCell(new Paragraph("FIRMA_______________________", defaultFont));
        cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellFirma.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        tabellaFirme.addCell(cellFirma);

        PdfPCell cellCapitano = new PdfPCell(new Paragraph("IL CAPITANO", new com.lowagie.text.Font(Font.HELVETICA, 10)));
        cellCapitano.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCapitano.setBorder(Rectangle.NO_BORDER);
        tabellaFirme.addCell(cellCapitano);
        document.add(tabellaFirme);

        /**
         * CHIUSURA DOCUMENTO
         */
        document.close();

        return risultato.toByteArray();
    }

}
