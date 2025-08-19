# Dashboard Pizzeria

Applicazione web **Spring Boot + Thymeleaf** per la gestione operativa di una pizzeria.  
L’app adotta un **layout a due colonne**: menu laterale sinistro (sezioni) e **contenuto dinamico a destra**, caricato **senza ricaricare la pagina** tramite `fetch` e **frammenti Thymeleaf**.

> **Stato**: progetto **in sviluppo** (work in progress). Nuove feature e refactor sono in corso.

![dashboard_pizzeria_uml](https://github.com/user-attachments/assets/dc3d8553-776d-450e-acc2-dee24a13bcfd)


# 
## Funzionalità

-   **Dashboard a due colonne**
    
    -   Sidebar (Profilo, Contratti attivi, Fornitori, Report, Logout)
        
    -   Area contenuti dinamica (frammenti Thymeleaf iniettati via `fetch`)
        
-   **Fornitori (Suppliers)**
    
    -   Elenco, creazione, modifica, cancellazione
        
    -   Upload/Download di **moduli d’ordine** (PDF/Excel)
        
    -   Campi: nome, email, ccEmails, deliveryDays, notes, orderFormFilename
        
-   **Contratti dipendenti (Employee Contracts)**
    
    -   Elenco, creazione, cancellazione
        
    -   Dettaglio contratto con upload/eliminazione documenti collegati
        
-   **Sicurezza**
    
    -   Integrazione **Spring Security CSRF** (token in meta + gestione in fetch/FormData)
        
-   **UI**
    
    -   **Bootstrap 5** via WebJars
        
    -   Template puliti, nessun CSS custom obbligatorio

## 
## Architettura

-   **Spring Boot / MVC**
    
    -   Controller specifici per dominio (`SupplierController`, `EmployeeContractController`)
        
    -   I controller **ritornano frammenti Thymeleaf** (es. `fragments/suppliers/index :: index`)
        
-   **Thymeleaf**
    
    -   `templates/dashboard.html` (layout principale)
        
    -   `templates/fragments/**` (solo markup “interno” con `th:fragment`)
        
-   **Fetch (AJAX nativo)**
    
    -   `static/js/dashboard.js` gestisce:
        
        -   click del menu (sidebar)
            
        -   click dei link interni ai frammenti
            
        -   submit dei form (anche **multipart**)
            
        -   gestione **CSRF**
            
        -   piccolo spinner di caricamento
            
-   **Persistenza**
    
    -   Spring Data JPA + Hibernate
        
-   **File system**
    
    -   Upload salvati in `uploads/` e `uploads/contracts/`

## 
## Sicurezza & CSRF

-   Spring Security inserisce il token CSRF nel model (`_csrf.token`, `_csrf.headerName`).
    
-   **Dashboard** mette il token in `<meta>` per l’uso globale in `fetch`.
    
-   Nei form inviati con `FormData` il token viene **aggiunto automaticamente** dallo script:
    
    -   nel **body** (`_csrf`)
        
    -   e come **header** (`X-CSRF-TOKEN` o nome header configurato)
## 
## Upload dei file

-   Cartelle:
    
    -   `uploads/` per i moduli fornitore
        
    -   `uploads/contracts/` per i documenti dei contratti
        
-   Accertarsi che le directory esistano o che il codice crei `Files.createDirectories(...)`
    
-   Endpoint di download restituisce `ResponseEntity<Resource>` con `Content-Disposition: attachment`

## 
## Setup & Avvio

### Requisiti

-   JDK 17+ (consigliato)
    
-   Maven o Gradle
    
-   DB (a scelta: H2 per dev, Postgres/MySQL per prod)

## 
## Testing (spunti)

-   Test MVC dei controller per verificare che ritornino i **frammenti** corretti
    
-   Test di persistenza su repository (Supplier, EmployeeContract, EmployeeDocument)
    
-   Test integrazione upload/download file


# 
## Roadmap

-   Completare **Report** (dataset, viste, esport)
    
-   Sezione **Profilo**
    
-   Paginazione e ricerca in Suppliers/Contracts
    
-   Validazioni lato server + messaggi utente (BindingResult)
    
-   Migliorare feedback UI (toast, spinner contestuali)
    
-   Ruoli/permessi (autorizzazioni granulari)


