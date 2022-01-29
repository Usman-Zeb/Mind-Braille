package com.example.mindbraille.globals;

import android.app.Application;

public class GlobalClass extends Application {

    private boolean InMain;
    private boolean InMenu;
    private boolean InNewSMS;
    private boolean InCallMenu;
    private boolean InCallPhoneBook;
    private boolean Blinked;
    private int BlinkValue;
    private int ConcentrationValue;

    public void setInMain(boolean InMain) {this.InMain = InMain;}
    public void setInMenu(boolean InMenu) {this.InMenu = InMenu;}
    public void setInNewSMS(boolean InNewSMS) {this.InNewSMS = InNewSMS;}
    public void setInCallMenu(boolean InCallMenu) {this.InCallMenu = InCallMenu;}
    public void setInCallPhoneBook(boolean InCallPhoneBook) {this.InCallPhoneBook = InCallPhoneBook;}
    public void setBlinked(boolean Blinked) {this.Blinked = Blinked;}
    public void setBlinkValue(int BlinkValue) {this.BlinkValue = BlinkValue;}
    public void setConcentrationValue(int ConcentrationValue) {this.ConcentrationValue = ConcentrationValue;}

    public boolean getInMain() {return this.InMain;}
    public boolean getInMenu() {return this.InMenu;}
    public boolean getInNewSMS() {return this.InNewSMS;}
    public boolean getInCallMenu() {return this.InCallMenu;}
    public boolean getInCallPhoneBook() {return this.InCallPhoneBook;}
    public boolean getBlinked() {return this.Blinked;}
    public int getBlinkValue() {return this.BlinkValue;}
    public int getConcentrationValue() {return this.ConcentrationValue;}
}
