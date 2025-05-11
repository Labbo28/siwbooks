INSERT INTO utente (id,nome, cognome, email, password, ruolo)
VALUES
(0,'Pippo', 'Adminetti', 'pippo.admin@siwbooks.it',
 'Pippo123',2),

(1,'Pluto', 'Useroni', 'pluto.user@siwbooks.it',
 'Pluto123',1);

 
SELECT setval('utente_seq', 2, false);
