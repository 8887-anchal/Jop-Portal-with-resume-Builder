# Resume Builder — Setup Instructions

## Files provided:
- `resume-builder.component.html`  → Angular template
- `resume-builder.component.ts`    → Angular component logic
- `resume-builder.component.css`   → Styles
- `ResumeGeneratorController.java` → Spring Boot PDF generator

---

## STEP 1: Add iText PDF dependency to Spring Boot

Open `pom.xml` and add inside `<dependencies>`:

```xml
<dependency>
    <groupId>com.lowagie</groupId>
    <artifactId>itext</artifactId>
    <version>2.1.7</version>
</dependency>
```

OR use the newer iTextPDF (recommended):

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13.3</version>
</dependency>
```

---

## STEP 2: Add Spring Boot controller

Copy `ResumeGeneratorController.java` to:
```
src/main/java/com/example/demo/Controller/ResumeGeneratorController.java
```

Also add `/resume/generate` to the JwtFilter public URLs in `JwtFilter.java`:
```java
private static final List<String> PUBLIC_URLS = List.of(
    "/users/login", "/users/register", "/users", "/resume/generate"
);
```

---

## STEP 3: Add Angular component

Create folder:
```
src/app/pages/resume-builder/
```

Copy the 3 files there:
- `resume-builder.component.html`
- `resume-builder.component.ts`
- `resume-builder.component.css`

---

## STEP 4: Add route in Angular

In `app.routes.ts`, add:
```typescript
{
  path: 'resume-builder',
  loadComponent: () =>
    import('./pages/resume-builder/resume-builder.component')
      .then(m => m.ResumeBuilderComponent),
  canActivate: [AuthGuard]
}
```

---

## STEP 5: Add link in dashboard/navbar

```html
<a routerLink="/resume-builder">Build Resume</a>
```

---

## How it works:
1. User fills the form on the Angular page
2. Clicks "Download PDF Resume"
3. Angular sends JSON data to `POST http://localhost:8082/resume/generate`
4. Spring Boot generates a professional PDF using iTextPDF
5. PDF downloads automatically in the browser
