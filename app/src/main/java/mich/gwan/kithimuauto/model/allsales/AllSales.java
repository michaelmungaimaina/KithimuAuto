package mich.gwan.kithimuauto.model.allsales;

public class AllSales {
    String saleDate;
    String saleTime;
    String salecategory;
    int saleQnty;
    int saleUprice;
    int saleTotal;
    int saleProfit;
    int saleId;

    public void setSaleDate(String saleDate) {this.saleDate = saleDate;}
    public String getSaleDate() {return saleDate;}

    public void setSaleTime(String saleTime) {this.saleTime = saleTime;}
    public String getSaleTime() {
        return saleTime;
    }

    public void setSalecategory(String salecategory) {
        this.salecategory = salecategory;
    }
    public String getSalecategory() {
        return salecategory;
    }

    public void setSaleQnty(int saleQnty) {
        this.saleQnty = saleQnty;
    }
    public int getSaleQnty() {
        return saleQnty;
    }

    public int getSaleProfit() {
        return saleProfit;
    }
    public void setSaleProfit(int saleProfit) {
        this.saleProfit = saleProfit;
    }

    public int getSaleTotal() {
        return saleTotal;
    }
    public void setSaleTotal(int saleTotal) {
        this.saleTotal = saleTotal;
    }

    public int getSaleUprice() {
        return saleUprice;
    }
    public void setSaleUprice(int saleUprice) {
        this.saleUprice = saleUprice;
    }
    public int getSaleId(){ return saleId; }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
}
