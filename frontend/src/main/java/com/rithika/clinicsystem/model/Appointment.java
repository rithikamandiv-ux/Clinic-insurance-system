package com.rithika.clinicsystem.model;

public class Appointment {
    //used constant values for status
    public static final String STATUS_BOOKED = "BOOKED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private String appointmentId;
    private String date;
    private String status;
    private String doctorId;
    private String patientId;

    public Appointment(String appointmentId, String date, String status, String doctorId, String patientId){
        this.appointmentId = appointmentId;
        this.date = date;
        this.status = status;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

     public String getAppointmentId(){
        return appointmentId;
     }

     public String getDate(){
        return date;
     }

     public String getStatus(){
        return status;
     }

     public String getDoctorId(){
        return doctorId;
     }

     public String getPatientId(){
        return patientId;
     }

     public void setDate(String date){
        this.date = date;
     }

     public void setStatus(String status){
        this.status = status;
     }

     public void displayAppointment(){
        System.out.println("Appointment No : " + appointmentId);
        System.out.println("patient Id : " + patientId);
        System.out.println("doctor Id : " + doctorId);
        System.out.println("date : " + date);
        System.out.println("Appointment status : " + status);
     }

}
