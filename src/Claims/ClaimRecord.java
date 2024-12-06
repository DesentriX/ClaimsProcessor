package Claims;

public class ClaimRecord {
    private String Product;
    private int Origin_year;
    private int Development_Year;
    private double Incremental_Value;

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public int getOrigin_year() {
        return Origin_year;
    }

    public void setOrigin_year(int origin_year) {
        Origin_year = origin_year;
    }

    public int getDevelopment_Year() {
        return Development_Year;
    }

    public void setDevelopment_Year(int development_Year) {
        Development_Year = development_Year;
    }

    public double getIncremental_Value() {
        return Incremental_Value;
    }

    public void setIncremental_Value(double incremental_Value) {
        Incremental_Value = incremental_Value;
    }

    public ClaimRecord(String Product, int Origin_year, int Development_Year, double Incremental_Value) {
        this.Product = Product;
        this.Origin_year = Origin_year;
        this.Development_Year = Development_Year;
        this.Incremental_Value = Incremental_Value;
    }

    public String toString() {
        return Product + " , " + Origin_year + " , " + Development_Year + " , " + Incremental_Value;
    }


}
