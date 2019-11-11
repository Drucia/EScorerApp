package com.druciak.escorerapp.model.generateSheetService;

import android.os.Environment;

import com.druciak.escorerapp.entities.Action;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchPlayer;
import com.druciak.escorerapp.entities.MatchTeam;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.entities.Shift;
import com.druciak.escorerapp.entities.Time;
import com.druciak.escorerapp.interfaces.IGenerateSheetMVP;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SheetGenerator {
    private MatchInfo matchInfo;
    private IGenerateSheetMVP.IModel callback;

    public SheetGenerator(MatchInfo matchInfo, IGenerateSheetMVP.IModel callback) {
        this.matchInfo = matchInfo;
        this.callback = callback;
    }

    public void generateSheet() {
        try {
            String fpath = Environment.getExternalStorageDirectory()
                    + "/sheet.pdf";
            File file = new File(fpath);

            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fpath));
            pdfDoc.setDefaultPageSize(PageSize.A3.rotate());
            Document doc = new Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER, PdfEncodings.UTF8);
            doc.setFont(font);
            Table bigTable = new Table(2);
            bigTable.addCell(generateMatchInfoTable());
            bigTable.addCell(generateMatchInfoTimeTable().setMarginLeft(10f));
            doc.add(generateTeamsTable()).setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(bigTable);
            doc.add(generateMatchScoreTable());
            doc.add(generatePlayersTable());
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Table generateTeamsTable() {
        Table table = new Table(4);
        table.addCell(new String(" A ".getBytes(StandardCharsets.UTF_8)));
        table.addCell(matchInfo.getTeamA().getFullName());
        table.addCell(matchInfo.getTeamB().getFullName());
        table.addCell(" B ");
        return table;
    }

    private Table generateMatchInfoTable() {
        Table table = new Table(6);

        // 1 row
        table.addCell(new Cell(1, 1).add(new Paragraph("Nazwa")));
        table.addCell(new Cell(1, 5).add(new Paragraph(matchInfo.getSettings().getMatch().getName())));

        // 2 row
        table.addCell(new Cell(1, 1).add(new Paragraph("Miasto")));
        table.addCell(new Cell(1, 5).add(new Paragraph(matchInfo.getSettings()
                .getTown() + ", " + matchInfo.getSettings().getStreet())));

        // 3 row
        table.addCell(new Cell(1, 1).add(new Paragraph("Sala")));
        table.addCell(new Cell(1, 5).add(new Paragraph(matchInfo.getSettings().getHall())));

        // 4 row
        table.addCell(new Cell(1, 1).add(new Paragraph("Plec")));
        table.addCell(new Cell(1, 1).setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(matchInfo.getSettings().isMan()
                ? "Mezczyzni" : "Kobiety")));
        table.addCell(new Cell(1, 1).add(new Paragraph("Kategoria")));
        table.addCell(new Cell(1, 1).setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(matchInfo.getSettings().getType())));
        table.addCell(new Cell(1, 1).add(new Paragraph("Faza")));
        table.addCell(new Cell(1, 1).setTextAlignment(TextAlignment.CENTER)
                .add(new Paragraph(matchInfo.getSettings().isZas()
                ? "ZAS" : "FIN")));
        return table;
    }

    private Table generateMatchInfoTimeTable() {
        Table table = new Table(2);
        table.addCell("Dzien");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        table.addCell(dtf.format(now));

        dtf = DateTimeFormatter.ofPattern("HH:ss");
        now = LocalDateTime.now();
        table.addCell("Godzina");
        table.addCell(dtf.format(now));

        table.addCell("Nr meczu");
        table.addCell(String.valueOf(matchInfo.getSettings().getMatch().getId()));
        return table;
    }

    private Table generateMatchScoreTable() {
        Table table = new Table(8).setTextAlignment(TextAlignment.CENTER);

        // 1 row
        table.addCell(new Cell(1, 8).add(new Paragraph("WYNIKI")));
        // 2 row
        table.addCell(new Cell(1, 4)
                .add(new Paragraph(matchInfo.getTeamA().getShortName())));
        table.addCell(new Cell(1, 4)
                .add(new Paragraph(matchInfo.getTeamB().getShortName())));
        // 3 row
        table.addCell("Czasy");
        table.addCell("Zmiany");
        table.addCell("Punkty");
        table.addCell(new Cell(1,2).add(new Paragraph("Set")));
        table.addCell("Punkty");
        table.addCell("Zmiany");
        table.addCell("Czasy");

        Map<Integer, Integer> times = matchInfo.getTimesOfSets();
        Map<Integer, SetInfo> result = new HashMap<>();
        Map<Integer, ArrayList<Action>> actionsOfSets = matchInfo.getActionsOfSets();
        MatchTeam teamA = matchInfo.getTeamA();
        MatchTeam teamB = matchInfo.getTeamB();
        for (Integer set : actionsOfSets.keySet())
        {
            ArrayList<Action> actions = actionsOfSets.get(set);
            if (!actions.isEmpty()) {
                int shiftsOfTeamA = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == teamA.getTeamId()).count();
                int shiftsOfTeamB = (int) actions.stream().filter(action -> action instanceof Shift
                        && action.getTeamMadeActionId() == teamB.getTeamId()).count();
                int timesOfTeamA = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == teamA.getTeamId()).count();
                int timesOfTeamB = (int) actions.stream().filter(action -> action instanceof Time
                        && action.getTeamMadeActionId() == teamB.getTeamId()).count();
                Action lastAction = actions.get(actions.size() - 1);
                int pointsOfA = lastAction.getTeamMadeActionId() == teamA.getTeamId() ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();
                int pointsOfB = lastAction.getTeamMadeActionId() == teamB.getTeamId() ?
                        lastAction.getTeamMadeActionPoints() : lastAction.getSndTeamPoints();

                SetInfo setInfo = new SetInfo(shiftsOfTeamA, shiftsOfTeamB, timesOfTeamA, timesOfTeamB,
                        pointsOfA, pointsOfB, set, times.get(set));
                result.put(set, setInfo);
            }
        }

        int pointsACounter = 0;
        int pointsBCounter = 0;
        int shiftsACounter = 0;
        int shiftsBCounter = 0;
        int timesACounter = 0;
        int timesBCounter = 0;
        int setsTimesCounter = 0;

        for (Integer set : result.keySet())
        {
            SetInfo info = result.get(set);
            table.addCell(String.valueOf(info.getTimesA()));
            table.addCell(String.valueOf(info.getShiftsA()));
            table.addCell(String.valueOf(info.getPointsA()));
            table.addCell(new Cell(1,2)
                    .add(new Paragraph(info.getSet() + " ( " + info.getTime() + " min )")));
            table.addCell(String.valueOf(info.getPointsB()));
            table.addCell(String.valueOf(info.getShiftsB()));
            table.addCell(String.valueOf(info.getTimesB()));

            timesACounter += info.getTimesA();
            timesBCounter += info.getTimesB();
            shiftsACounter += info.getShiftsA();
            shiftsBCounter += info.getShiftsB();
            pointsACounter += info.getPointsA();
            pointsBCounter += info.getPointsB();
            setsTimesCounter += info.getTime();
        }

        table.addCell(String.valueOf(timesACounter));
        table.addCell(String.valueOf(shiftsACounter));
        table.addCell(String.valueOf(pointsACounter));
        table.addCell(new Cell(1,2)
                .add(new Paragraph("Razem ( " + setsTimesCounter + " min )")));
        table.addCell(String.valueOf(pointsBCounter));
        table.addCell(String.valueOf(shiftsBCounter));
        table.addCell(String.valueOf(timesBCounter));

        return table;
    }

    private Table generatePlayersTable() {
        Table table = new Table(6).setTextAlignment(TextAlignment.CENTER);
        // 1 row
        table.addCell(new Cell(1, 6).add(new Paragraph("ZAWODNICY")));
        // 2 row
        table.addCell(" A ");
        table.addCell(new Cell(1, 2)
                .add(new Paragraph(matchInfo.getTeamA().getShortName())));
        table.addCell(new Cell(1, 2)
                .add(new Paragraph(matchInfo.getTeamB().getShortName())));
        table.addCell(" B ");
        // 3 row
        table.addCell(" Nr ");
        table.addCell(new Cell(1, 2)
                .add(new Paragraph("Nazwa")));
        table.addCell(" Nr ");
        table.addCell(new Cell(1, 2)
                .add(new Paragraph("Nazwa")));

        List<MatchPlayer> playersAWithoutLibero = matchInfo.getTeamA().getPlayers().stream()
                .filter(matchPlayer -> !matchPlayer.isLibero())
                .sorted(Comparator.comparingInt(Player::getNumber))
                .collect(Collectors.toList());
        List<MatchPlayer> playersBWithoutLibero = matchInfo.getTeamB().getPlayers().stream()
                .filter(matchPlayer -> !matchPlayer.isLibero())
                .sorted(Comparator.comparingInt(Player::getNumber))
                .collect(Collectors.toList());

        int rows = Math.max(playersAWithoutLibero.size(), playersBWithoutLibero.size());

        for (int i = 0; i < rows; i++)
        {
            MatchPlayer matchPlayerA = i < playersAWithoutLibero.size() ? playersAWithoutLibero.get(i) : null;
            MatchPlayer matchPlayerB = i < playersAWithoutLibero.size() ? playersBWithoutLibero.get(i) : null;
            if (matchPlayerA == null) {
                table.addCell("");
                table.addCell(new Cell(1, 2));
            } else {
                table.addCell(String.valueOf(matchPlayerA.getNumber()));
                table.addCell(new Cell(1, 2).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(matchPlayerA.getSurname() + " " + matchPlayerA.getName())));
            }

            if (matchPlayerB == null) {
                table.addCell("");
                table.addCell(new Cell(1, 2));
            } else {
                table.addCell(String.valueOf(matchPlayerB.getNumber()));
                table.addCell(new Cell(1, 2).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(matchPlayerB.getSurname() + " " + matchPlayerB.getName())));
            }
        }

        return table;
    }
}
