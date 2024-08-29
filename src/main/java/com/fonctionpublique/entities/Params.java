package com.fonctionpublique.entities;

import java.net.URI;

public interface Params {
    public static final String SECRET_KEY = "3AgC5BMpo1qa/FZ4GQsKxd03rJMQkvrBnWNBCsDbHroEmbtld8x285bDM11Os/Ym\n";
    public static final String jwtSigningKey = "3AgC5BMpo1qa/FZ4GQsKxd03rJMQkvrBnWNBCsDbHroEmbtld8x285bDM11Os/Ym\n";
    public static final int EXP_DATE = 1000 * 60 * 24;
    //les attestations gnenerees

//    public static final String DIRECTORYATTESTATION = "/home/adminadie/Desktop/attestation";
//    public static final String DIRECTORYCNI = "/home/adminadie/Desktop/cni";
//    public static final String DIRECTORYQRCOD = "/home/adminadie/Desktop/qrcode";
//    public static final String DIRECTORYSIGNATURE = "/home/adminadie/Desktop/signature";
//    public static final String DIRECTORYRESOURCE = "/home/adminadie/Desktop/resource";
//    public static final String LIENDEVERIFICATION = "fpublique-demarche.sec.gouv.sn/#/verifierscan/";
//    public static final String LIENRESTPASSWORD = "fpublique-demarche.sec.gouv.sn//#/new-password/";
//    public static final String EMAILSENDER = "fpublique2024@gmail.com";


    /*public static final String DIRECTORYATTESTATION = "/home/ubuntu/Desktop/attestation";
    public static final String DIRECTORYCNI = "/home/ubuntu/Desktop/cni";
    public static final String DIRECTORYQRCOD = "/home/ubuntu/Desktop/qrcode";
    public static final String DIRECTORYSIGNATURE = "/home/ubuntu/Desktop/signature";
    public static final String DIRECTORYRESOURCE = "/home/ubuntu/Desktop/resource";
    public static final String LIENDEVERIFICATION = "http://51.79.71.34:8080/angular/#/verifierscan/";
    public static final String LIENRESTPASSWORD = "http://51.79.71.34:8080/angular/#/new-password/";
    public static final String EMAILSENDER = "fpublique2024@gmail.com";*/


    public static final String DIRECTORYATTESTATION = System.getProperty("user.home")+"/Desktop";///Downloads/attestations";"/home/adminadie/Desktop/attestation";//
    public static final String DIRECTORYCNI = System.getProperty("user.home")+"/Desktop";///Attestation/Attestation/uploaded/documents";"/home/adminadie/Desktop/cni";//
    public static final String DIRECTORYQRCOD = System.getProperty("user.home")+"/Desktop";//"/Users/7maksacodpc/Downloads/The_QRCode/";"/home/adminadie/Desktop/qrcode/";
    public static final String DIRECTORYSIGNATURE = System.getProperty("user.home")+"/Desktop";//"/Users/7maksacodpc/Downloads/The_QRCode/";"/home/adminadie/Desktop/qrcode/";
    public static final String DIRECTORYRESOURCE = System.getProperty("user.home")+"/Desktop";//"/Users/7maksacodpc/Downloads/The_QRCode/";"/home/adminadie/Desktop/qrcode/";
    public static final String LIENDEVERIFICATION = "https://localhost:4200/#/verifierscan/";
    public static final String LIENRESTPASSWORD = "https://localhost:4200/#/new-password/";
    public static final String EMAILSENDER = "fpublique2024@gmail.com";




    public static final String PREFIX = "Nº ";
}
