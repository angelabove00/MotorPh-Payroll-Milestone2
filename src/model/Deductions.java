package model;

public class Deductions {
    private double sss;
    private double philHealth;
    private double pagIbig;
    private double withholdingTax;
    private double otherDeductions;

    public double getSss() { 
        return sss; 
    }

    public void setSss(double sss) { 
        this.sss = sss; 
    }

    public double getPhilHealth() { 
        return philHealth; 
    }

    public void setPhilHealth(double philHealth) { 
        this.philHealth = philHealth; 
    }

    public double getPagIbig() { 
        return pagIbig; 
    }

    public void setPagIbig(double pagIbig) { 
        this.pagIbig = pagIbig; 
    }

    public double getWithholdingTax() { 
        return withholdingTax; 
    }

    public void setWithholdingTax(double withholdingTax) { 
        this.withholdingTax = withholdingTax; 
    }

    public double getOtherDeductions() { 
        return otherDeductions; 
    }

    public void setOtherDeductions(double otherDeductions) { 
        this.otherDeductions = otherDeductions; 
    }

    public double getTotalDeductions() {
        return sss + philHealth + pagIbig + withholdingTax + otherDeductions;
    }
} 
