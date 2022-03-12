package com.example.alphatour.oggetti;

public class Elemento {


    private String titolo,descrizione,foto,codiceQr,attivita,codiceSensore;

    public Elemento(){

    }



    public Elemento(String titolo,String descrizione,String foto,String codiceQr,String attivita,String codiceSensore){

        this.titolo=titolo;
        this.descrizione=descrizione;
        this.foto=foto;
        this.codiceQr=codiceQr;
        this.attivita=attivita;
        this.codiceSensore=codiceSensore;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setCodiceQr(String codiceQr) {
        this.codiceQr = codiceQr;
    }

    public void setAttivita(String attivita) {
        this.attivita = attivita;
    }

    public void setCodiceSensore(String codiceSensore) {
        this.codiceSensore = codiceSensore;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getFoto() {
        return foto;
    }

    public String getCodiceQr() {
        return codiceQr;
    }

    public String getAttivita() {
        return attivita;
    }

    public String getCodiceSensore() {
        return codiceSensore;
    }


    @Override
    public String toString() {
        return "Elemento{" +
                "titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", foto='" + foto + '\'' +
                ", codiceQr='" + codiceQr + '\'' +
                ", attivita='" + attivita + '\'' +
                ", codiceSensore='" + codiceSensore +
                '}';
    }
}
