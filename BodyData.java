package com.afufu.controlbodyfat;

/**
 * Created by user on 2015/8/14.
 */
public class BodyData {
    int id;
    long Date;
    double Weight;
    double FatRate;
    double FatWeight;
    double Waistline;
    double Hips;

    public BodyData(int id ,long D,double W,double FR,double FW,double WL,double H){
        this.id=id;
        this.Date=D;
        this.Weight=W;
        this.FatRate=FR;
        this.FatWeight=FW;
        this.Waistline=WL;
        this.Hips=H;
    }

    public int getId(){
        return id;
    }

    public long getDate(){
        return Date;
    }

    public double getWeight()
    {
        return Weight;
    }

    public double getFatRate()
    {
        return FatRate;
    }

    public double getFatWeight()
    {
        return FatWeight;
    }

    public double getWaistline()
    {
        return Waistline;
    }
    public double getHips()
    {
        return Hips;
    }
}
