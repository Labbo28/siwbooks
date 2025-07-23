# Book Review System – Casi d'Uso

Questo sistema permette la gestione di libri, autori e recensioni da parte di utenti registrati e amministratori. Di seguito i principali casi d'uso supportati dall'applicazione.

## Requisito preliminare
> Per inserire un libro, **l'autore deve essere già registrato** nel database. Durante l'inserimento di un libro, è necessario selezionare l'autore da un elenco esistente.

---

## 👤 Amministratore

### 1. Inserimento di un nuovo libro o autore
- L'amministratore si autentica e accede alla sezione "Amministratore".
- Può inserire un nuovo autore o un nuovo libro.
- Il nuovo contenuto è immediatamente visibile a tutti gli utenti.

### 2. Modifica o cancellazione di un libro/autore esistente
- Dopo l'autenticazione, seleziona l'elemento da modificare dalla sezione "Gestione amministratore".
- Può modificare o rimuovere un autore o un libro.
- Se rimuove un libro, vengono eliminati anche le recensioni e le immagini associate (ma **non** gli autori collegati).
- Se rimuove un autore, i libri scritti da quell’autore non vengono eliminati ma risultano non associati.

### 3. Pulizia immagini orfane
- L'amministratore può eseguire una **pulizia forzata** delle immagini che non sono più collegate a nessun autore o libro.
- Questa operazione aiuta a mantenere pulito il file system e ottimizzare l'utilizzo dello storage.

---

## 🧑‍💻 Utente Registrato

### 4. Inserimento di una nuova recensione
- Dopo il login, l'utente seleziona un libro e scrive una recensione (titolo, voto, testo).
- Una volta pubblicata, la recensione è visibile a tutti dalla pagina del libro.

> ⚠️ Un utente può inserire **una sola recensione per ciascun libro**.

### 5. Modifica di una recensione
- L’utente autenticato può modificare la propria recensione.

### 6. Eliminazione di una recensione
- L'utente autenticato può eliminare la propria recensione

---

## 👥 Utente Occasionale (Non Registrato)

### 6. Visualizzazione informazioni su un autore
- L’utente può accedere alla lista autori e visitare la pagina dedicata a ciascuno.
- Può consultare informazioni base (nome, immagine, data di nascita, eventuale data di morte) e vedere tutti i libri pubblicati.

### 7. Visualizzazione recensioni di un libro
- Può visualizzare tutte le recensioni associate a un libro, con dettagli su autore della recensione, titolo, voto e testo.
- Se prova a scrivere una recensione, viene invitato a effettuare il login.

---

[Visualizza il PDF](./siwbooks.pdf)

