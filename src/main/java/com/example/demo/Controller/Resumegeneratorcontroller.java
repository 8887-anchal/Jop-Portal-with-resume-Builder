package com.example.demo.Controller;

//ResumeGeneratorController.java
//Place at: src/main/java/com/example/demo/Controller/ResumeGeneratorController.java


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@RestController
@RequestMapping("/resume")
public class Resumegeneratorcontroller {

 // ── Fonts ────────────────────────────────────────────────────────────────
 private static final Font NAME_FONT    = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD,   BaseColor.BLACK);
 private static final Font TITLE_FONT   = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, new BaseColor(60, 60, 180));
 private static final Font CONTACT_FONT = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, new BaseColor(80, 80, 80));
 private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   BaseColor.BLACK);
 private static final Font BODY_FONT    = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, BaseColor.BLACK);
 private static final Font BOLD_FONT    = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   BaseColor.BLACK);
 private static final Font ITALIC_FONT  = new Font(Font.FontFamily.HELVETICA,  9, Font.ITALIC, new BaseColor(80, 80, 80));

 @PostMapping("/generate")
 public ResponseEntity<byte[]> generateResume(@RequestBody Map<String, Object> data) throws Exception {

     Document doc = new Document(PageSize.A4, 50, 50, 45, 45);
     ByteArrayOutputStream out = new ByteArrayOutputStream();
     PdfWriter.getInstance(doc, out);
     doc.open();

     // ── HEADER ───────────────────────────────────────────────────────────
     String name     = str(data, "name");
     String jobTitle = str(data, "title");
     String location = str(data, "location");
     String phone    = str(data, "phone");
     String email    = str(data, "email");
     String linkedin = str(data, "linkedin");
     String github   = str(data, "github");

     Paragraph namePara = new Paragraph(name, NAME_FONT);
     namePara.setAlignment(Element.ALIGN_CENTER);
     doc.add(namePara);

     String subtitle = joinNonEmpty(" | ", jobTitle, location);
     if (!subtitle.isEmpty()) {
         Paragraph subPara = new Paragraph(subtitle, TITLE_FONT);
         subPara.setAlignment(Element.ALIGN_CENTER);
         subPara.setSpacingBefore(2);
         doc.add(subPara);
     }

     String contactLine = joinNonEmpty("  ·  ", phone, email, linkedin, github);
     if (!contactLine.isEmpty()) {
         Paragraph contactPara = new Paragraph(contactLine, CONTACT_FONT);
         contactPara.setAlignment(Element.ALIGN_CENTER);
         contactPara.setSpacingBefore(3);
         doc.add(contactPara);
     }

     addDivider(doc);

     // ── SUMMARY ──────────────────────────────────────────────────────────
     String summary = str(data, "summary");
     if (!summary.isEmpty()) {
         addSectionTitle(doc, "PROFESSIONAL SUMMARY");
         doc.add(new Paragraph(summary, BODY_FONT));
         doc.add(Chunk.NEWLINE);
     }

     // ── TECHNICAL SKILLS ─────────────────────────────────────────────────
     @SuppressWarnings("unchecked")
     List<Map<String, String>> skillCats = (List<Map<String, String>>) data.get("skillCategories");
     if (skillCats != null) {
         boolean hasSkills = skillCats.stream().anyMatch(c -> !c.getOrDefault("value", "").trim().isEmpty());
         if (hasSkills) {
             addSectionTitle(doc, "TECHNICAL SKILLS");
             for (Map<String, String> cat : skillCats) {
                 String val = cat.getOrDefault("value", "").trim();
                 if (!val.isEmpty()) {
                     Paragraph p = new Paragraph();
                     p.add(new Chunk(cat.getOrDefault("label", "") + ": ", BOLD_FONT));
                     p.add(new Chunk(val, BODY_FONT));
                     p.setSpacingBefore(2);
                     doc.add(p);
                 }
             }
             doc.add(Chunk.NEWLINE);
         }
     }

     // ── EXPERIENCE ───────────────────────────────────────────────────────
     @SuppressWarnings("unchecked")
     List<Map<String, String>> experiences = (List<Map<String, String>>) data.get("experience");
     if (experiences != null && !experiences.isEmpty()) {
         addSectionTitle(doc, "PROFESSIONAL EXPERIENCE");
         for (Map<String, String> exp : experiences) {
             // Title line
             PdfPTable titleRow = new PdfPTable(2);
             titleRow.setWidthPercentage(100);
             titleRow.setWidths(new float[]{70, 30});
             titleRow.getDefaultCell().setBorder(Rectangle.NO_BORDER);
             titleRow.getDefaultCell().setPadding(0);

             String expTitle   = exp.getOrDefault("title", "");
             String expCompany = exp.getOrDefault("company", "");
             String expLoc     = exp.getOrDefault("location", "");

             Paragraph leftCell = new Paragraph();
             leftCell.add(new Chunk(expTitle, BOLD_FONT));
             if (!expCompany.isEmpty()) leftCell.add(new Chunk(" — " + expCompany, BODY_FONT));

             Paragraph rightCell = new Paragraph();
             rightCell.add(new Chunk(joinNonEmpty(", ", exp.getOrDefault("duration",""), expLoc), ITALIC_FONT));
             rightCell.setAlignment(Element.ALIGN_RIGHT);

             PdfPCell lc = new PdfPCell(leftCell);
             lc.setBorder(Rectangle.NO_BORDER); lc.setPadding(0);
             PdfPCell rc = new PdfPCell(rightCell);
             rc.setBorder(Rectangle.NO_BORDER); rc.setPadding(0);

             titleRow.addCell(lc);
             titleRow.addCell(rc);
             titleRow.setSpacingBefore(6);
             doc.add(titleRow);

             // Bullets
             String bulletsRaw = exp.getOrDefault("bullets", "");
             for (String bullet : bulletsRaw.split("\n")) {
                 String b = bullet.trim();
                 if (!b.isEmpty()) {
                     Paragraph bp = new Paragraph("• " + b, BODY_FONT);
                     bp.setIndentationLeft(12);
                     bp.setSpacingBefore(1);
                     doc.add(bp);
                 }
             }
         }
         doc.add(Chunk.NEWLINE);
     }

     // ── PROJECTS ─────────────────────────────────────────────────────────
     @SuppressWarnings("unchecked")
     List<Map<String, String>> projects = (List<Map<String, String>>) data.get("projects");
     if (projects != null && !projects.isEmpty()) {
         addSectionTitle(doc, "PROJECTS");
         for (Map<String, String> proj : projects) {
             Paragraph projTitle = new Paragraph();
             projTitle.add(new Chunk(proj.getOrDefault("name", ""), BOLD_FONT));
             String tech = proj.getOrDefault("tech", "").trim();
             if (!tech.isEmpty()) projTitle.add(new Chunk("  " + tech, ITALIC_FONT));
             projTitle.setSpacingBefore(5);
             doc.add(projTitle);

             String bulletsRaw = proj.getOrDefault("bullets", "");
             for (String bullet : bulletsRaw.split("\n")) {
                 String b = bullet.trim();
                 if (!b.isEmpty()) {
                     Paragraph bp = new Paragraph("• " + b, BODY_FONT);
                     bp.setIndentationLeft(12);
                     bp.setSpacingBefore(1);
                     doc.add(bp);
                 }
             }
         }
         doc.add(Chunk.NEWLINE);
     }

     // ── EDUCATION ────────────────────────────────────────────────────────
     @SuppressWarnings("unchecked")
     List<Map<String, String>> education = (List<Map<String, String>>) data.get("education");
     if (education != null && !education.isEmpty()) {
         addSectionTitle(doc, "EDUCATION");
         for (Map<String, String> edu : education) {
             Paragraph p = new Paragraph();
             p.add(new Chunk(edu.getOrDefault("degree", ""), BOLD_FONT));
             p.setSpacingBefore(5);
             doc.add(p);

             Paragraph p2 = new Paragraph();
             p2.add(new Chunk(edu.getOrDefault("institution", ""), BODY_FONT));
             String year  = edu.getOrDefault("year", "").trim();
             String score = edu.getOrDefault("score", "").trim();
             if (!year.isEmpty())  p2.add(new Chunk("  " + year,         ITALIC_FONT));
             if (!score.isEmpty()) p2.add(new Chunk("  |  " + score,     ITALIC_FONT));
             doc.add(p2);
         }
         doc.add(Chunk.NEWLINE);
     }

     // ── CERTIFICATIONS ───────────────────────────────────────────────────
     @SuppressWarnings("unchecked")
     List<Map<String, String>> certs = (List<Map<String, String>>) data.get("certifications");
     if (certs != null && !certs.isEmpty()) {
         addSectionTitle(doc, "CERTIFICATIONS & ACHIEVEMENTS");
         for (Map<String, String> cert : certs) {
             Paragraph p = new Paragraph();
             p.add(new Chunk("• " + cert.getOrDefault("title", ""), BOLD_FONT));
             String issuer = cert.getOrDefault("issuer", "").trim();
             String date   = cert.getOrDefault("date", "").trim();
             if (!issuer.isEmpty()) p.add(new Chunk(" — " + issuer, BODY_FONT));
             if (!date.isEmpty())   p.add(new Chunk(", " + date, ITALIC_FONT));
             p.setSpacingBefore(3);
             doc.add(p);
         }
     }

     doc.close();

     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_PDF);
     headers.setContentDispositionFormData("attachment", name.replace(" ", "_") + "_Resume.pdf");
     return ResponseEntity.ok().headers(headers).body(out.toByteArray());
 }

 // ── Helpers ──────────────────────────────────────────────────────────────

 private void addSectionTitle(Document doc, String title) throws DocumentException {
     Paragraph p = new Paragraph(title, SECTION_FONT);
     p.setSpacingBefore(4);
     p.setSpacingAfter(2);
     doc.add(p);

     // Underline via a thin line
     PdfPTable line = new PdfPTable(1);
     line.setWidthPercentage(100);
     PdfPCell cell = new PdfPCell();
     cell.setBorderWidthTop(0); cell.setBorderWidthLeft(0); cell.setBorderWidthRight(0);
     cell.setBorderWidthBottom(1.2f);
     cell.setBorderColorBottom(BaseColor.BLACK);
     cell.setPadding(0); cell.setFixedHeight(1);
     line.addCell(cell);
     line.setSpacingAfter(5);
     doc.add(line);
 }

 private void addDivider(Document doc) throws DocumentException {
     PdfPTable line = new PdfPTable(1);
     line.setWidthPercentage(100);
     PdfPCell cell = new PdfPCell();
     cell.setBorderWidthTop(0); cell.setBorderWidthLeft(0); cell.setBorderWidthRight(0);
     cell.setBorderWidthBottom(2f);
     cell.setBorderColorBottom(BaseColor.BLACK);
     cell.setPadding(0); cell.setFixedHeight(1);
     line.addCell(cell);
     line.setSpacingBefore(6);
     line.setSpacingAfter(8);
     doc.add(line);
 }

 private String str(Map<String, Object> data, String key) {
     Object val = data.get(key);
     return val != null ? val.toString().trim() : "";
 }

 private String joinNonEmpty(String sep, String... parts) {
     StringBuilder sb = new StringBuilder();
     for (String part : parts) {
         if (part != null && !part.trim().isEmpty()) {
             if (sb.length() > 0) sb.append(sep);
             sb.append(part.trim());
         }
     }
     return sb.toString();
 }
}