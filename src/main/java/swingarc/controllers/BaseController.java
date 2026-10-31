package swingarc.controllers;

public abstract class BaseController {
    protected MasterController masterController;

    public void setMasterController(MasterController masterController) {
        this.masterController = masterController;
    }
}
