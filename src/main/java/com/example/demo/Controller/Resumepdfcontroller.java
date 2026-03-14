package com.example.demo.Controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/resume")
public class Resumepdfcontroller {

    // Fonts
    private static Font nameFont;
    private static Font titleFont;
    private static Font sectionFont;
    private static Font boldFont;
    private static Font normalFont;
    private static Font smallFont;
    private static Font italicFont;

    static {
        try {
            BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, false);
            BaseFont baseBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, false);
            BaseFont baseItalic = BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, false);

            nameFont    = new Font(baseBold,    22, Font.NORMAL, new BaseColor(26,  26, 46));
            titleFont   = new Font(base,         11, Font.NORMAL, new BaseColor(85,  85, 85));
            sectionFont = new Font(baseBold,     10, Font.NORMAL, new BaseColor(26,  26, 46));
            boldFont    = new Font(baseBold,      9, Font.NORMAL, new BaseColor(26,  26, 46));
            normalFont  = new Font(base,          9, Font.NORMAL, new BaseColor(50,  50, 50));
            smallFont   = new Font(base,          8, Font.NORMAL, new BaseColor(100,100,100));
            italicFont  = new Font(baseItalic,    8, Font.NORMAL, new BaseColor(120,120,120));
        } catch (Exception e) {
            nameFont    = FontFactory.getFont(FontFactory.HELVETICA_BOLD,   22);
            titleFont   = FontFactory.getFont(FontFactory.HELVETICA,        11);
            sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,   10);
            boldFont    = FontFactory.getFont(FontFactory.HELVETICA_BOLD,    9);
            normalFont  = FontFactory.getFont(FontFactory.HELVETICA,         9);
            smallFont   = FontFactory.getFont(FontFactory.HELVETICA,         8);
            italicFont  = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
        }
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestBody Map<String, Object> data) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // ---- HEADER ----
            Paragraph namePara = new Paragraph(getString(data, "name", "Your Name"), nameFont);
            namePara.setAlignment(Element.ALIGN_CENTER);
            doc.add(namePara);

            String title = getString(data, "title", "");
            if (!title.isEmpty()) {
                Paragraph titlePara = new Paragraph(title, titleFont);
                titlePara.setAlignment(Element.ALIGN_CENTER);
                titlePara.setSpacingBefore(2);
                doc.add(titlePara);
            }

            // Contact line
            StringBuilder contact = new StringBuilder();
            appendContact(contact, data, "location");
            appendContact(contact, data, "phone");
            appendContact(contact, data, "email");
            appendContact(contact, data, "linkedin");
            appendContact(contact, data, "github");
            if (contact.length() > 0) {
                Paragraph contactPara = new Paragraph(contact.toString().trim(), smallFont);
                contactPara.setAlignment(Element.ALIGN_CENTER);
                contactPara.setSpacingBefore(4);
                doc.add(contactPara);
            }

            addHRule(doc, new BaseColor(26, 26, 46), 1.5f, 6f, 4f);

            // ---- SUMMARY ----
            String summary = getString(data, "summary", "");
            if (!summary.isEmpty()) {
                addSectionHeader(doc, "PROFESSIONAL SUMMARY");
                Paragraph sumPara = new Paragraph(summary, normalFont);
                sumPara.setSpacingBefore(4);
                doc.add(sumPara);
            }

            // ---- SKILLS ----
            List<Map<String, String>> skills = getList(data, "skills");
            if (!skills.isEmpty()) {
                addSectionHeader(doc, "TECHNICAL SKILLS");
                for (Map<String, String> skill : skills) {
                    String cat = skill.getOrDefault("category", "");
                    String items = skill.getOrDefault("items", "");
                    if (!cat.isEmpty() && !items.isEmpty()) {
                        Paragraph sp = new Paragraph();
                        sp.add(new Chunk(cat + ": ", boldFont));
                        sp.add(new Chunk(items, normalFont));
                        sp.setSpacingBefore(3);
                        doc.add(sp);
                    }
                }
            }

            // ---- EXPERIENCE ----
            List<Map<String, Object>> experience = getListObj(data, "experience");
            if (!experience.isEmpty()) {
                addSectionHeader(doc, "PROFESSIONAL EXPERIENCE");
                for (Map<String, Object> exp : experience) {
                    String role = getString(exp, "role", "");
                    String company = getString(exp, "company", "");
                    String location = getString(exp, "location", "");
                    String duration = getString(exp, "duration", "");

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{3f, 1f});
                    table.setSpacingBefore(6);

                    Paragraph left = new Paragraph();
                    left.add(new Chunk(role, boldFont));
                    if (!company.isEmpty()) {
                        left.add(new Chunk(" — " + company, normalFont));
                        if (!location.isEmpty()) left.add(new Chunk(", " + location, normalFont));
                    }
                    PdfPCell leftCell = new PdfPCell(left);
                    leftCell.setBorder(Rectangle.NO_BORDER);
                    leftCell.setPadding(0);

                    Paragraph right = new Paragraph(duration, italicFont);
                    right.setAlignment(Element.ALIGN_RIGHT);
                    PdfPCell rightCell = new PdfPCell(right);
                    rightCell.setBorder(Rectangle.NO_BORDER);
                    rightCell.setPadding(0);
                    rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    table.addCell(leftCell);
                    table.addCell(rightCell);
                    doc.add(table);

                    List<String> points = getStringList(exp, "points");
                    for (String pt : points) {
                        if (pt != null && !pt.isEmpty()) {
                            Paragraph bullet = new Paragraph("\u2022  " + pt, normalFont);
                            bullet.setIndentationLeft(12);
                            bullet.setSpacingBefore(2);
                            doc.add(bullet);
                        }
                    }
                }
            }

            // ---- PROJECTS ----
            List<Map<String, Object>> projects = getListObj(data, "projects");
            if (!projects.isEmpty()) {
                addSectionHeader(doc, "PROJECTS");
                for (Map<String, Object> proj : projects) {
                    String pName = getString(proj, "name", "");
                    String tech  = getString(proj, "tech", "");

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{3f, 1.5f});
                    table.setSpacingBefore(6);

                    PdfPCell leftCell = new PdfPCell(new Phrase(pName, boldFont));
                    leftCell.setBorder(Rectangle.NO_BORDER);
                    leftCell.setPadding(0);

                    Paragraph techPara = new Paragraph(tech, italicFont);
                    techPara.setAlignment(Element.ALIGN_RIGHT);
                    PdfPCell rightCell = new PdfPCell(techPara);
                    rightCell.setBorder(Rectangle.NO_BORDER);
                    rightCell.setPadding(0);
                    rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    table.addCell(leftCell);
                    table.addCell(rightCell);
                    doc.add(table);

                    List<String> points = getStringList(proj, "points");
                    for (String pt : points) {
                        if (pt != null && !pt.isEmpty()) {
                            Paragraph bullet = new Paragraph("\u2022  " + pt, normalFont);
                            bullet.setIndentationLeft(12);
                            bullet.setSpacingBefore(2);
                            doc.add(bullet);
                        }
                    }
                }
            }

            // ---- EDUCATION ----
            List<Map<String, Object>> education = getListObj(data, "education");
            if (!education.isEmpty()) {
                addSectionHeader(doc, "EDUCATION");
                for (Map<String, Object> edu : education) {
                    String degree = getString(edu, "degree", "");
                    String inst   = getString(edu, "institution", "");
                    String year   = getString(edu, "year", "");
                    String score  = getString(edu, "score", "");

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{3f, 1f});
                    table.setSpacingBefore(6);

                    Paragraph left = new Paragraph();
                    left.add(new Chunk(degree, boldFont));
                    if (!inst.isEmpty()) left.add(new Chunk(" — " + inst, normalFont));
                    PdfPCell leftCell = new PdfPCell(left);
                    leftCell.setBorder(Rectangle.NO_BORDER);
                    leftCell.setPadding(0);

                    Paragraph right = new Paragraph();
                    right.add(new Chunk(year + "\n", italicFont));
                    if (!score.isEmpty()) right.add(new Chunk(score, smallFont));
                    right.setAlignment(Element.ALIGN_RIGHT);
                    PdfPCell rightCell = new PdfPCell(right);
                    rightCell.setBorder(Rectangle.NO_BORDER);
                    rightCell.setPadding(0);
                    rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    table.addCell(leftCell);
                    table.addCell(rightCell);
                    doc.add(table);
                }
            }

            // ---- CERTIFICATIONS ----
            List<String> certs = getStringListDirect(data, "certifications");
            if (!certs.isEmpty()) {
                addSectionHeader(doc, "CERTIFICATIONS & ACHIEVEMENTS");
                for (String cert : certs) {
                    if (cert != null && !cert.isEmpty()) {
                        Paragraph bullet = new Paragraph("\u2022  " + cert, normalFont);
                        bullet.setIndentationLeft(12);
                        bullet.setSpacingBefore(3);
                        doc.add(bullet);
                    }
                }
            }

            doc.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = getString(data, "name", "resume").replaceAll("\\s+", "_") + "_resume.pdf";
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // ---- Helpers ----
    private void addSectionHeader(Document doc, String title) throws DocumentException {
        Paragraph header = new Paragraph(title, sectionFont);
        header.setSpacingBefore(10);
        header.setSpacingAfter(2);
        doc.add(header);
        addHRule(doc, new BaseColor(232, 197, 71), 1f, 0f, 4f);
    }

    private void addHRule(Document doc, BaseColor color, float width, float spaceBefore, float spaceAfter) throws DocumentException {
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.setSpacingBefore(spaceBefore);
        line.setSpacingAfter(spaceAfter);
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthBottom(width);
        cell.setBorderColorBottom(color);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setPadding(0);
        line.addCell(cell);
        doc.add(line);
    }

    private void appendContact(StringBuilder sb, Map<String, Object> data, String key) {
        String val = getString(data, key, "");
        if (!val.isEmpty()) {
            if (sb.length() > 0) sb.append("  |  ");
            sb.append(val);
        }
    }

    @SuppressWarnings("unchecked")
    private String getString(Map<String, ?> map, String key, String def) {
        Object v = map.get(key);
        return (v instanceof String s && !s.isBlank()) ? s : def;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> getList(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return (v instanceof List) ? (List<Map<String, String>>) v : List.of();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getListObj(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return (v instanceof List) ? (List<Map<String, Object>>) v : List.of();
    }

    @SuppressWarnings("unchecked")
    private List<String> getStringList(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return (v instanceof List) ? (List<String>) v : List.of();
    }

    @SuppressWarnings("unchecked")
    private List<String> getStringListDirect(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return (v instanceof List) ? (List<String>) v : List.of();
    }
}