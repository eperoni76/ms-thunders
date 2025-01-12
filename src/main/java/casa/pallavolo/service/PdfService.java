package casa.pallavolo.service;

import casa.pallavolo.dto.DatiGaraDTO;
import casa.pallavolo.dto.GiocatoreDTO;
import casa.pallavolo.model.AddettoDefibrillatore;
import casa.pallavolo.model.Dirigente;
import casa.pallavolo.utils.Constants;
import casa.pallavolo.utils.Paths;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private GiocatoreService giocatoreService;
    @Autowired
    private AddettoDefibrillatoreService addettoDefibrillatoreService;
    @Autowired
    private DirigenteService dirigenteService;

    public byte[] generaListaGara(DatiGaraDTO datiGara) throws IOException {
        /**
         * APERTURA DOCUMENTO
         */
        ByteArrayOutputStream risultato = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, risultato);
        document.open();
        Color darkGreen = new Color(0, 128, 0);
        Color darkRed = new Color(139, 0, 0);
        DateTimeFormatter dataEOraFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter dataNascitaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        /**
         * CARICAMENTO IMMAGINI
         */
        Image image1 = Image.getInstance(Paths.UISP_IMAGE_1);
        Image image2 = Image.getInstance(Paths.UISP_IMAGE_2);
        Image image3 = Image.getInstance(Paths.UISP_IMAGE_3);

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

        PdfPCell cell1 = new PdfPCell(new Phrase(Constants.TITOLO_PDF, new Font(Font.HELVETICA, 14, Font.NORMAL, darkGreen)));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setBorder(Rectangle.NO_BORDER);
        title1.addCell(cell1);
        title1.setSpacingAfter(10f);
        document.add(title1);


        PdfPTable title2 = new PdfPTable(1);
        title2.setWidthPercentage(100);
        PdfPCell cell2 = new PdfPCell(new Phrase(Constants.PRIMO_SOTTOTITOLO, new Font(Font.HELVETICA, 11, Font.BOLD, darkGreen)));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(Rectangle.BOX);
        cell2.setPadding(5f);
        title2.addCell(cell2);
        title2.setSpacingAfter(10f);
        document.add(title2);


        PdfPTable title3 = new PdfPTable(1);
        title3.setWidthPercentage(100);
        Chunk chunkSquadra1 = new Chunk(Constants.SECONDO_SOTTOTITOLO, new Font(Font.HELVETICA, 11, Font.BOLD, darkGreen));
        Chunk chunkSquadra2 = new Chunk("          " + Constants.NOME_SQUADRA, new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK));
        Phrase squadra = new Phrase();
        squadra.add(chunkSquadra1);
        squadra.add(chunkSquadra2);
        PdfPCell cell3 = new PdfPCell(squadra);

        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setBorder(Rectangle.BOX);
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

        Chunk chunk1_1 = new Chunk("N^ GARA", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk1_2 = new Chunk("    " + datiGara.getNumeroGara(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell1 = new PdfPCell();
        Phrase phrase1 = new Phrase();
        phrase1.add(chunk1_1);
        phrase1.add(chunk1_2);
        garaCell1.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell1.setPadding(2f);
        garaCell1.addElement(phrase1);
        gara1.addCell(garaCell1);

        Chunk chunk2_1 = new Chunk("OSPITANTE", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk2_2 = new Chunk("    " + datiGara.getOspitante(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell2 = new PdfPCell();
        Phrase phrase2 = new Phrase();
        phrase2.add(chunk2_1);
        phrase2.add(chunk2_2);
        garaCell2.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell2.setPadding(2f);
        garaCell2.addElement(phrase2);
        gara1.addCell(garaCell2);

        Chunk chunk3_1 = new Chunk("OSPITE", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk3_2 = new Chunk("    " + datiGara.getOspite(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell3 = new PdfPCell();
        Phrase phrase3 = new Phrase();
        phrase3.add(chunk3_1);
        phrase3.add(chunk3_2);
        garaCell3.setVerticalAlignment(Element.ALIGN_CENTER);
        garaCell3.setPadding(2f);
        garaCell3.addElement(phrase3);
        gara1.addCell(garaCell3);
        document.add(gara1);

        PdfPTable gara2 = new PdfPTable(3);
        gara2.setWidthPercentage(100);
        gara2.setWidths(new float[]{0.2f, 0.4f, 0.4f});

        Chunk chunk4_1 = new Chunk("LOCALITÀ", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk4_2 = new Chunk("    " + datiGara.getLocalita(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell4 = new PdfPCell();
        Phrase phrase4 = new Phrase();
        phrase4.add(chunk4_1);
        phrase4.add(chunk4_2);
        garaCell4.setPadding(2f);
        garaCell4.addElement(phrase4);
        gara2.addCell(garaCell4);

        Chunk chunk5_1 = new Chunk("IMPIANTO", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk5_2 = new Chunk("    " + datiGara.getImpianto(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
        PdfPCell garaCell5 = new PdfPCell();
        Phrase phrase5 = new Phrase();
        phrase5.add(chunk5_1);
        phrase5.add(chunk5_2);
        garaCell5.setPadding(2f);
        garaCell5.addElement(phrase5);
        gara2.addCell(garaCell5);

        Chunk chunk6_1 = new Chunk("DATA E ORA", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk6_2 = new Chunk("    " + dataEOraFormatter.format(datiGara.getDataOra()), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
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

        Chunk chunk7_1 = new Chunk("CAMPIONATO", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen));
        Chunk chunk7_2 = new Chunk("    " + datiGara.getCampionato(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK));
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
        List<GiocatoreDTO> giocatori = giocatoreService.getAllGiocatori();
        PdfPTable tableGiocatori = new PdfPTable(5);
        tableGiocatori.setWidthPercentage(100);
        Font titleFont = new Font(Font.HELVETICA, 11, Font.BOLD, darkGreen);
        Font nomiFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
        Font dataFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);
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
                nomeCognomeCell.setBorder(Rectangle.NO_BORDER);
                nomeCognomeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nestedTable.addCell(nomeCognomeCell);

                String aggiunta = giocatore.getIsCapitano() ? "K" : "L";
                PdfPCell liberoCell = new PdfPCell(new Phrase(aggiunta, nomiFont));
                liberoCell.setBorder(Rectangle.NO_BORDER);
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

        PdfPCell cell1Allenatore = new PdfPCell(new Phrase("1^ ALLENATORE", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen)));
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
        PdfPCell allenatoreCellValue = new PdfPCell(new Phrase(primoAllenatoreNome.toString(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK)));
        allenatoreCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        allenatoreCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(allenatoreCellValue);

        PdfPCell allenatoreTesseraCellValue = new PdfPCell(new Phrase(primoAllenatoreTesseraUisp, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        allenatoreTesseraCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        allenatoreTesseraCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(allenatoreTesseraCellValue);

        tableDirigenti.addCell("");

        PdfPCell cell2Allenatore = new PdfPCell(new Phrase("2^ ALLENATORE", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen)));
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
        PdfPCell secondoAllenatoreCellValue = new PdfPCell(new Phrase(secondoAllenatoreNome.toString(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK)));
        secondoAllenatoreCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        secondoAllenatoreCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(secondoAllenatoreCellValue);

        PdfPCell secondoAllenatoreTesseraCellValue = new PdfPCell(new Phrase(secondoAllenatoreTesseraUisp, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        secondoAllenatoreTesseraCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        secondoAllenatoreTesseraCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(secondoAllenatoreTesseraCellValue);

        tableDirigenti.addCell("");

        PdfPCell cellDirigente = new PdfPCell(new Phrase("DIRIGENTE", new Font(Font.HELVETICA, 9, Font.BOLD, darkGreen)));
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
        PdfPCell dirigenteCellValue = new PdfPCell(new Phrase(dirigenteNome.toString().toUpperCase(), new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK)));
        dirigenteCellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        dirigenteCellValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableDirigenti.addCell(dirigenteCellValue);

        PdfPCell dirigenteTesseraCellValue = new PdfPCell(new Phrase(dirigenteTesseraUisp, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
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

        Font redBoldFont1 = new Font(Font.HELVETICA, 12, Font.BOLD, darkRed);
        Font redBoldFont2 = new Font(Font.HELVETICA, 10, Font.BOLD, darkRed);

        PdfPCell cellPrimaRiga = new PdfPCell(new Phrase(Constants.TITOLO_DEFIBRILLATORE, redBoldFont1));
        cellPrimaRiga.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellPrimaRiga.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellPrimaRiga.setBorder(Rectangle.BOX);
        cellPrimaRiga.setFixedHeight(35f);
        tableInformazioni.addCell(cellPrimaRiga);

        PdfPCell cellSecondaRiga = new PdfPCell(new Phrase(Constants.SOTTOTITOLO_DEFIBRILLATORE, redBoldFont2));
        cellSecondaRiga.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSecondaRiga.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellSecondaRiga.setBorder(Rectangle.BOX);
        cellSecondaRiga.setFixedHeight(35f);
        tableInformazioni.addCell(cellSecondaRiga);
        tableInformazioni.setSpacingAfter(10f);
        document.add(tableInformazioni);

        /**
         * TABELLA DICHIARAZIONE DA FIRMARE
         */
        Font defaultFont = new Font(Font.HELVETICA, 10,  Font.BOLD, Color.BLACK);
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
        cellSottoscritto.setBorder(Rectangle.NO_BORDER);
        tablePrima.addCell(cellSottoscritto);

        PdfPCell cellCodiceFiscale = new PdfPCell(codiceFiscale);
        cellCodiceFiscale.setBorder(Rectangle.NO_BORDER);
        tablePrima.addCell(cellCodiceFiscale);
        tablePrima.setSpacingAfter(5f);
        document.add(tablePrima);

        PdfPTable tableSeconda = new PdfPTable(3);
        tableSeconda.setWidthPercentage(100);
        tableSeconda.setWidths(new float[]{0.3f, 0.2f, 0.5f});

        PdfPCell cellNatoIl = new PdfPCell(dataNascita);
        cellNatoIl.setBorder(Rectangle.NO_BORDER);
        tableSeconda.addCell(cellNatoIl);

        PdfPCell cellA = new PdfPCell(luogoNascita);
        cellA.setBorder(Rectangle.NO_BORDER);
        tableSeconda.addCell(cellA);

        PdfPCell cellResidenteIn = new PdfPCell(luogoResidenza);
        cellResidenteIn.setBorder(Rectangle.NO_BORDER);
        tableSeconda.addCell(cellResidenteIn);
        tableSeconda.setSpacingAfter(5f);
        document.add(tableSeconda);

        PdfPTable tableTerza = new PdfPTable(1);
        tableTerza.setWidthPercentage(100);

        PdfPCell cellConsapevole = new PdfPCell(new Phrase(Constants.TESTO_DICHIARAZIONE_DEFIBRILLATORE, defaultFont));
        cellConsapevole.setBorder(Rectangle.NO_BORDER);
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
        cellData.setBorder(Rectangle.NO_BORDER);
        tabellaFirme.addCell(cellData);

        PdfPCell cellFirma = new PdfPCell(new Paragraph("FIRMA_______________________", defaultFont));
        cellFirma.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellFirma.setBorder(Rectangle.NO_BORDER);
        tabellaFirme.addCell(cellFirma);

        PdfPCell cellCapitano = new PdfPCell(new Paragraph("IL CAPITANO", new Font(Font.HELVETICA, 10)));
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
