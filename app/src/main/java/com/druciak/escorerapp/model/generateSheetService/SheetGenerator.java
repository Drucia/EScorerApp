package com.druciak.escorerapp.model.generateSheetService;

import android.os.Environment;
import android.util.Log;

import com.druciak.escorerapp.entities.Action;
import com.druciak.escorerapp.entities.LineUp;
import com.druciak.escorerapp.entities.MatchInfo;
import com.druciak.escorerapp.entities.MatchPlayer;
import com.druciak.escorerapp.entities.MatchTeam;
import com.druciak.escorerapp.entities.Player;
import com.druciak.escorerapp.entities.SetInfo;
import com.druciak.escorerapp.entities.Shift;
import com.druciak.escorerapp.entities.TeamAdditionalMember;
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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class SheetGenerator {
    private static final Integer SHIFT_NUMBER = 1;
    private static final Integer COME_BACK = 2;
    private static final Integer SHIFT_SCORE = 3;
    private static final Integer COME_BACK_SCORE = 4;

    private MatchInfo matchInfo;
    private IGenerateSheetMVP.IModel callback;
    private String scorerName;

    public SheetGenerator(MatchInfo matchInfo, IGenerateSheetMVP.IModel callback, String scorerName) {
        this.matchInfo = matchInfo;
        this.callback = callback;
        this.scorerName = scorerName;
    }

    public void generateSheet() {
        String fpath = Environment.getExternalStorageDirectory()
                + "/Protokoly";
        File dir = new File(fpath);
        File file = new File(dir, "sheet_" + Math.abs(new Random().nextInt()) + ".pdf");

        PdfDocument pdfDoc = null;
        Document doc = null;

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.createNewFile();

            pdfDoc = new PdfDocument(new PdfWriter(file));
            pdfDoc.setDefaultPageSize(PageSize.A2.rotate());
            doc = new Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER, PdfEncodings.UTF8);
            doc.setFont(font);
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .setBorder(Border.NO_BORDER)
                    .useAllAvailableWidth();
            infoTable.addCell(new Cell(4, 1).add(generateMatchInfoTable().useAllAvailableWidth()));
            infoTable.addCell(new Cell(4, 1).add(generateTeamsTable().useAllAvailableWidth()));
            infoTable.addCell(new Cell(4, 1).add(generateMatchInfoTimeTable().useAllAvailableWidth()));
            doc.add(infoTable);
            Table setsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
            generateSetsTables(setsTable);
            doc.add(setsTable);
            infoTable = new Table(2);
            infoTable.addCell(new Cell(1, 1).add(generateAttentionTable()
                    .useAllAvailableWidth()));
            infoTable.addCell(new Cell(2,1).add(generatePlayersTable()));
            infoTable.addCell(new Cell(1,1).add(generateMatchScoreTable()
                    .useAllAvailableWidth()));
            doc.add(infoTable);
            doc.close();

        } catch (IOException e) {
            Log.d("WRITE PDF", e.getMessage());
        } finally {
            if (pdfDoc != null)
                pdfDoc.close();
            if (doc != null)
                doc.close();
        }
        callback.sheetGenerated(file);
    }

    private void generateSetsTables(Table mainTable) {
        MatchTeam leftTeam = matchInfo.getTeamB();
        MatchTeam rightTeam = matchInfo.getTeamA();
        for (Integer set : matchInfo.getActionsOfSets().keySet()) {
            MatchTeam tmp = leftTeam;
            leftTeam = rightTeam;
            rightTeam = tmp;
            ArrayList<Action> actions = matchInfo.getActionsOfSets().get(set);
            if (!actions.isEmpty()) {
                int teamServeId = matchInfo.getServesOfSets().get(set);
                mainTable.addCell(generateSet(set, actions, leftTeam, rightTeam,
                        teamServeId == leftTeam.getTeamId()));
            }
        }
    }

    private Table generateSet(int set, ArrayList<Action> actions, MatchTeam left, MatchTeam right,
                              boolean isLeftServe) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{ 1, 2, 1, 1, 2, 2, 2, 1, 1,
                2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2})).setTextAlignment(TextAlignment.CENTER);

        // set number
        Cell title = new Cell(9, 1);
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("S"));
        title.add(new Paragraph("E"));
        title.add(new Paragraph("T"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph("\n"));
        title.add(new Paragraph(String.valueOf(set)));
        table.addCell(title);

        // set info
        // 1 row
        Cell time = new Cell(1, 2);
        time.add(new Paragraph("START"));
        time.add(new Paragraph("8:00")); // todo
        table.addCell(time);
        table.addCell(new Cell(1, 5).add(new Paragraph(left.getShortName())));
        Cell serve = new Cell();
        serve.add(new Paragraph(isLeftServe ? "S" : "-"));
        serve.add(new Paragraph(isLeftServe ? "-" : "R"));
        table.addCell(serve);
        table.addCell(new Cell(1, 2).add(new Paragraph("PUNKTY")));
        serve = new Cell();
        serve.add(new Paragraph(isLeftServe ? "-" : "S"));
        serve.add(new Paragraph(isLeftServe ? "R" : "-"));
        table.addCell(serve);
        table.addCell(new Cell(1, 5).add(new Paragraph(right.getShortName())));
        time = new Cell(1, 2);
        time.add(new Paragraph("KONIEC"));
        time.add(new Paragraph("8:20")); // todo
        table.addCell(time);
        table.addCell(new Cell(1, 2).add(new Paragraph("PUNKTY")));

        Action last = actions.get(actions.size()-1);
        int pointsLeft = last.getTeamMadeActionId() == left.getTeamId() ?
                last.getTeamMadeActionPoints() : last.getSndTeamPoints();
        int pointsRight = last.getTeamMadeActionId() == right.getTeamId() ?
                last.getTeamMadeActionPoints() : last.getSndTeamPoints();

        // 2 row - info row
        generateInfoRow(table, pointsLeft, pointsRight);

        List<Action> leftLineUp = actions.stream().filter(action -> action instanceof LineUp &&
                (action.getTeamMadeActionId() == left.getTeamId())).collect(Collectors.toList());
        List<Action> rightLineUp = actions.stream().filter(action -> action instanceof LineUp &&
                (action.getTeamMadeActionId() == right.getTeamId())).collect(Collectors.toList());
        // 3 row - line up row
        generateLineUp(table, leftLineUp, rightLineUp);

        List<Action> leftShifts = actions.stream().filter(action -> action instanceof Shift &&
                action.getTeamMadeActionId() == left.getTeamId()).collect(Collectors.toList());
        List<Action> rightShifts = actions.stream().filter(action -> action instanceof Shift &&
                action.getTeamMadeActionId() == right.getTeamId()).collect(Collectors.toList());
        // 3-5 rows - shift rows
        generateShifts(table, leftLineUp, rightLineUp, leftShifts, rightShifts);

        List<Action> leftTimes = actions.stream().filter(action -> action instanceof Time &&
                action.getTeamMadeActionId() == left.getTeamId()).collect(Collectors.toList());
        List<Action> rightTimes = actions.stream().filter(action -> action instanceof Time &&
                action.getTeamMadeActionId() == right.getTeamId()).collect(Collectors.toList());
        // 6-8 rows - times rows
        generateTimes(table, leftTimes, rightTimes);
        return table;
    }

    private void generateTimes(Table table, List<Action> leftTimes, List<Action> rightTimes) {
        // ----- LEFT SIDE ----- //
        table.addCell(new Cell(3, 8).add(new Paragraph("in the future ..."))
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Cell(1, 2).add(new Paragraph("CZASY")));

        // ----- RIGHT SIDE ----- //
        table.addCell(new Cell(3, 8).add(new Paragraph("in the future ..."))
                .setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Cell(1, 2).add(new Paragraph("CZASY")));

        for (int i = 0; i < 2; i++) {
            Time left = i < leftTimes.size() ? ((Time) leftTimes.get(i)) : null;
            Time right = i < rightTimes.size() ? ((Time) rightTimes.get(i)) : null;

            table.addCell(new Cell(1, 2).add(new Paragraph(left == null ? ":"
                    : left.getTeamMadeActionPoints() + ":" + left.getSndTeamPoints())));
            table.addCell(new Cell(1, 2).add(new Paragraph(right == null ? ":"
                    : right.getTeamMadeActionPoints() + ":" + right.getSndTeamPoints())));
        }
    }

    private void generateShifts(Table table, List<Action> leftLineUp, List<Action> rightLineUp,
                                List<Action> leftShifts, List<Action> rightShifts) {
        // ----- LEFT SIDE ----- //
        Map<Integer, String> oneL = getShiftMapFor(getLineUpForArea(1, leftLineUp), leftShifts);
        table.addCell(oneL.get(SHIFT_NUMBER) + oneL.get(COME_BACK));
        Map<Integer, String> twoL = getShiftMapFor(getLineUpForArea(2, leftLineUp), leftShifts);
        table.addCell(new Cell(1, 2).add(new Paragraph(twoL.get(SHIFT_NUMBER) + twoL.get(COME_BACK))));
        Map<Integer, String> threeL = getShiftMapFor(getLineUpForArea(3, leftLineUp), leftShifts);
        table.addCell(threeL.get(SHIFT_NUMBER) + threeL.get(COME_BACK));
        Map<Integer, String> fourL = getShiftMapFor(getLineUpForArea(4, leftLineUp), leftShifts);
        table.addCell(fourL.get(SHIFT_NUMBER) + fourL.get(COME_BACK));
        Map<Integer, String> fiveL = getShiftMapFor(getLineUpForArea(5, leftLineUp), leftShifts);
        table.addCell(fiveL.get(SHIFT_NUMBER) + fiveL.get(COME_BACK));
        Map<Integer, String> sixL = getShiftMapFor(getLineUpForArea(6, leftLineUp), leftShifts);
        table.addCell(new Cell(1, 2).add(new Paragraph(sixL.get(SHIFT_NUMBER) + sixL.get(COME_BACK))));

        // ----- RIGHT SIDE ----- //
        Map<Integer, String> oneR = getShiftMapFor(getLineUpForArea(1, rightLineUp), rightShifts);
        table.addCell(new Cell(1, 2).add(new Paragraph(oneR.get(SHIFT_NUMBER) + oneR.get(COME_BACK))));
        Map<Integer, String> twoR = getShiftMapFor(getLineUpForArea(2, rightLineUp), rightShifts);
        table.addCell(twoR.get(SHIFT_NUMBER) + twoR.get(COME_BACK));
        Map<Integer, String> threeR = getShiftMapFor(getLineUpForArea(3, rightLineUp), rightShifts);
        table.addCell(threeR.get(SHIFT_NUMBER) + threeR.get(COME_BACK));
        Map<Integer, String> fourR = getShiftMapFor(getLineUpForArea(4, rightLineUp), rightShifts);
        table.addCell(fourR.get(SHIFT_NUMBER) + fourR.get(COME_BACK));
        Map<Integer, String> fiveR = getShiftMapFor(getLineUpForArea(5, rightLineUp), rightShifts);
        table.addCell(new Cell(1, 2).add(new Paragraph(fiveR.get(SHIFT_NUMBER) + fiveR.get(COME_BACK))));
        Map<Integer, String> sixR = getShiftMapFor(getLineUpForArea(6, rightLineUp), rightShifts);
        table.addCell(sixR.get(SHIFT_NUMBER) + sixR.get(COME_BACK));

        // ----- LEFT SIDE ----- //
        table.addCell(oneL.get(SHIFT_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(twoL.get(SHIFT_SCORE))));
        table.addCell(threeL.get(SHIFT_SCORE));
        table.addCell(fourL.get(SHIFT_SCORE));
        table.addCell(fiveL.get(SHIFT_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(sixL.get(SHIFT_SCORE))));

        // ----- RIGHT SIDE ----- //
        table.addCell(new Cell(1, 2).add(new Paragraph(oneR.get(SHIFT_SCORE))));
        table.addCell(twoR.get(SHIFT_SCORE));
        table.addCell(threeR.get(SHIFT_SCORE));
        table.addCell(fourR.get(SHIFT_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(fiveR.get(SHIFT_SCORE))));
        table.addCell(sixR.get(SHIFT_SCORE));

        // ----- LEFT SIDE ----- //
        table.addCell(oneL.get(COME_BACK_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(twoL.get(COME_BACK_SCORE))));
        table.addCell(threeL.get(COME_BACK_SCORE));
        table.addCell(fourL.get(COME_BACK_SCORE));
        table.addCell(fiveL.get(COME_BACK_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(sixL.get(COME_BACK_SCORE))));

        // ----- RIGHT SIDE ----- //
        table.addCell(new Cell(1, 2).add(new Paragraph(oneR.get(COME_BACK_SCORE))));
        table.addCell(twoR.get(COME_BACK_SCORE));
        table.addCell(threeR.get(COME_BACK_SCORE));
        table.addCell(fourR.get(COME_BACK_SCORE));
        table.addCell(new Cell(1, 2).add(new Paragraph(fiveR.get(COME_BACK_SCORE))));
        table.addCell(sixR.get(COME_BACK_SCORE));
    }

    private Map<Integer, String> getShiftMapFor(int startNumber, List<Action> shiftsActions) {
        Map<Integer, String> shiftMap = new HashMap<>();
        List<Action> shifts = shiftsActions.stream().filter(action -> ((Shift) action).getOutPlayerNb() == startNumber
        || ((Shift) action).getEnterPlayerNb() == startNumber).collect(Collectors.toList());

        shiftMap.put(SHIFT_NUMBER, "-");
        shiftMap.put(COME_BACK, "");
        shiftMap.put(SHIFT_SCORE, ":");
        shiftMap.put(COME_BACK_SCORE, ":");

        for (Action action : shifts)
        {
            Shift s = (Shift) action;
            if (s.getEnterPlayerNb() == startNumber) // come back_arrow shift
            {
                shiftMap.put(COME_BACK, "!");
                shiftMap.put(COME_BACK_SCORE, s.getTeamMadeActionPoints()
                        + ":" + s.getSndTeamPoints());
            } else if (s.getOutPlayerNb() == startNumber)
            {
                shiftMap.put(SHIFT_NUMBER, String.valueOf(s.getEnterPlayerNb()));
                shiftMap.put(SHIFT_SCORE, s.getTeamMadeActionPoints()
                        + ":" + s.getSndTeamPoints());
            }
        }
        return shiftMap;
    }

    private void generateLineUp(Table table, List<Action> leftLineUp, List<Action> rightLineUp) {
        // ----- LEFT SIDE ----- //
        table.addCell(String.valueOf(getLineUpForArea(1, leftLineUp)));
        table.addCell(new Cell(1, 2).add(new Paragraph(String.valueOf(getLineUpForArea(2, leftLineUp)))));
        table.addCell(String.valueOf(getLineUpForArea(3, leftLineUp)));
        table.addCell(String.valueOf(getLineUpForArea(4, leftLineUp)));
        table.addCell(String.valueOf(getLineUpForArea(4, leftLineUp)));
        table.addCell(new Cell(1, 2).add(new Paragraph(String.valueOf(getLineUpForArea(6, leftLineUp)))));

        // ----- RIGHT SIDE ----- //
        table.addCell(new Cell(1, 2).add(new Paragraph(String.valueOf(getLineUpForArea(1, rightLineUp)))));
        table.addCell(String.valueOf(getLineUpForArea(2, rightLineUp)));
        table.addCell(String.valueOf(getLineUpForArea(3, rightLineUp)));
        table.addCell(String.valueOf(getLineUpForArea(4, rightLineUp)));
        table.addCell(new Cell(1, 2).add(new Paragraph(String.valueOf(getLineUpForArea(5, rightLineUp)))));
        table.addCell(String.valueOf(getLineUpForArea(6, rightLineUp)));
    }

    private int getLineUpForArea(int area, List<Action> linesUp) {
        Optional<Action> lineUp = linesUp.stream().filter(action -> ((LineUp) action).getAreaNb() == area)
                .findAny();
        return lineUp.map(action -> ((LineUp) action).getEnterNb()).orElse(-1);
    }

    private void generateInfoRow(Table table, int leftPoints, int rightPoints) {
        // ----- LEFT SIDE ----- //
        table.addCell("I");
        table.addCell(new Cell(1, 2).add(new Paragraph("II")));
        table.addCell("III");
        table.addCell("IV");
        table.addCell("V");
        table.addCell(new Cell(1, 2).add(new Paragraph("VI")));
        table.addCell(new Cell(5, 2).add(new Paragraph(
                generateStringForPoints(leftPoints)).setTextAlignment(TextAlignment.LEFT)
                .setPaddingLeft(5).setPaddingRight(5)));

        // ----- RIGHT SIDE ----- //
        table.addCell(new Cell(1, 2).add(new Paragraph("I")));
        table.addCell("II");
        table.addCell("III");
        table.addCell("IV");
        table.addCell(new Cell(1, 2).add(new Paragraph("V")));
        table.addCell("VI");
        table.addCell(new Cell(5, 2).add(new Paragraph(
                generateStringForPoints(rightPoints)).setTextAlignment(TextAlignment.LEFT)
                .setPaddingLeft(5).setPaddingRight(5)));
    }

    private String generateStringForPoints(int points) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= points; i++) {
            sb.append(i);
            sb.append(" ");
        }
        return sb.toString();
    }

    private Table generateAttentionTable() {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 2}));
        table.addCell(new Cell(1, 5).add(new Paragraph("UWAGI")));
        table.addCell(new Cell(3, 5).add(new Paragraph(matchInfo.getAttentions())
                .setMinHeight(70).setTextAlignment(TextAlignment.LEFT)));
        table.addCell(new Cell(1, 2).add(new Paragraph("SEDZIA I")));
        table.addCell(new Cell(1, 3).add(new Paragraph(matchInfo.getSettings().getRefereeFirst())));
        table.addCell(new Cell(1, 2).add(new Paragraph("SEDZIA II")));
        table.addCell(new Cell(1, 3).add(new Paragraph(matchInfo.getSettings().getRefereeSnd())));
        table.addCell(new Cell(1, 2).add(new Paragraph("SEKRETARZ")));
        table.addCell(new Cell(1, 3).add(new Paragraph(scorerName)));

        List<String> lines = matchInfo.getSettings().getLineReferees().stream()
                .filter(s -> !s.equals("")).collect(Collectors.toList());
        if (!lines.isEmpty())
        {
            table.addCell(new Cell(1, 1).add(new Paragraph("LINIOWY I")));
            table.addCell(new Cell(1, 2).add(new Paragraph(lines.get(0))));
            table.addCell(new Cell(1, 1).add(new Paragraph("LINIOWY II")));
            table.addCell(new Cell(1, 1).add(new Paragraph(lines.get(1))));

            if (lines.size() > 2)
            {
                table.addCell(new Cell(1, 1).add(new Paragraph("LINIOWY III")));
                table.addCell(new Cell(1, 2).add(new Paragraph(lines.get(2))));
                table.addCell(new Cell(1, 1).add(new Paragraph("LINIOWY IV")));
                table.addCell(new Cell(1, 1).add(new Paragraph(lines.get(3))));
            }
        }
        return table;
    }

    private Table generateTeamsTable() {
        Table table = new Table(4).setTextAlignment(TextAlignment.CENTER);
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
                .add(new Paragraph(matchInfo.getSettings().isFin()
                ? "ZAS" : "FIN")));
        return table;
    }

    private Table generateMatchInfoTimeTable() {
        Table table = new Table(2);
        table.addCell("Dzien");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        table.addCell(dtf.format(now));

        table.addCell("Rozpoczecie");
        table.addCell(matchInfo.getSettings().getStartTime());

        table.addCell("Zakonczenie");
        table.addCell(matchInfo.getSettings().getStartTime());

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
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 1, 3, 1}))
                .setTextAlignment(TextAlignment.CENTER);
        // 1 row
        table.addCell(new Cell(1, 6).add(new Paragraph("ZAWODNICY")));
        // 2 row
        table.addCell(new Cell(1, 1).add(new Paragraph(" A ")));
        table.addCell(new Cell(1, 2)
                .add(new Paragraph(matchInfo.getTeamA().getShortName())));
        table.addCell(new Cell(1, 2)
                .add(new Paragraph(matchInfo.getTeamB().getShortName())));
        table.addCell(new Cell(1, 1).add(new Paragraph(" B ")));
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

        addPlayersRows(table, playersAWithoutLibero, playersBWithoutLibero, rows);

        List<MatchPlayer> playersALibero = matchInfo.getTeamA().getPlayers().stream()
                .filter(Player::isLibero)
                .sorted(Comparator.comparingInt(Player::getNumber))
                .collect(Collectors.toList());
        List<MatchPlayer> playersBLibero = matchInfo.getTeamB().getPlayers().stream()
                .filter(Player::isLibero)
                .sorted(Comparator.comparingInt(Player::getNumber))
                .collect(Collectors.toList());

        rows = Math.max(playersALibero.size(), playersBLibero.size());
        table.addCell(new Cell(1, 8).add(new Paragraph("LIBERO")));
        addPlayersRows(table, playersALibero, playersBLibero, rows);

        List<TeamAdditionalMember> membersA = matchInfo.getTeamA().getMembers()
                .stream().sorted(Comparator.comparingInt(TeamAdditionalMember::getMemberTypeId))
                .collect(Collectors.toList());
        List<TeamAdditionalMember> membersB = matchInfo.getTeamB().getMembers()
                .stream().sorted(Comparator.comparingInt(TeamAdditionalMember::getMemberTypeId))
                .collect(Collectors.toList());

        rows = Math.max(membersA.size(), membersB.size());
        table.addCell(new Cell(1, 6).add(new Paragraph("CZLONKOWIE DRUZYNY")));
        addAdditionalMembersRows(table, membersA, membersB, rows);

        return table;
    }

    private void addAdditionalMembersRows(Table table, List<TeamAdditionalMember> membersA,
                                          List<TeamAdditionalMember> membersB, int rows) {
        for (int i = 0; i < rows; i++)
        {
            TeamAdditionalMember memberA = i < membersA.size() ? membersA.get(i) : null;
            TeamAdditionalMember memberB = i < membersB.size() ? membersB.get(i) : null;

            if (memberA == null) {
                table.addCell(new Cell(1, 2));
            } else {
                table.addCell(new Cell(1, 2).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(memberA.getName())));
            }

            if (memberA != null)
                table.addCell(new Cell(1, 2).add(new Paragraph(memberA.toStringId())));
            else if (memberB != null)
                table.addCell(new Cell(1, 2).add(new Paragraph(memberB.toStringId())));

            if (memberB == null) {
                table.addCell(new Cell(1, 2));
            } else {
                table.addCell(new Cell(1, 2).setTextAlignment(TextAlignment.LEFT)
                        .add(new Paragraph(memberB.getName())));
            }
        }
    }

    private void addPlayersRows(Table table, List<MatchPlayer> playersA, List<MatchPlayer> playersB,
                                int rows) {
        for (int i = 0; i < rows; i++)
        {
            MatchPlayer matchPlayerA = i < playersA.size() ? playersA.get(i) : null;
            MatchPlayer matchPlayerB = i < playersB.size() ? playersB.get(i) : null;
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
    }
}
