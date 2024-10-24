package com.bankinc.api.commmon;

public class Constant {

    public static final int ID_PRODUCT_TYPE_CARD = 1;
    public static final int PRODUCT_ACTIVATE = 1;
    public static final int PRODUCT_INACTIVATE = 0;
    public static final int PRODUCT_BLOQUED = 0;
    public static final int PRODUCT_NO_BLOQUED = 1;
    public static final int ANULATE_TRANSACTION_LIMIT_TIME = 24;
    public static final int PRODUCT_TYPE_NUMBER_LENGTH = 6;
    public static final int  VALIDITY_TIME= 3;
    public static final String NO_EXIST_CLIENT_MESSAGE = "cliente inexistente";
    public static final String NO_EXIST_PRODUCT_TYPE_MESSAGE = "error en el tipo del producto";
    public static final String NO_EXIST_PRODUCT_MESSAGE = "Producto no existente";
    public static final String NO_EXIST_TRANSACTION_MESSAGE = "transaccion no existente";
    public static final String INVALID_LENGTH_ID_PRODUCT_TYPE= "El Id del producto debe ser de 6 digitos";
    public static final String NO_ACTIVE_PRODUCT_MESSAGE= "La tarjeta no ha sido activada";
    public static final String BLOQUED_PRODUCT_MESSAGE= "La tarjeta se encuentra bloqueada";
    public static final String EXPIRED_PRODUCT_MESSAGE= "La tarjeta se encuentra expirada";
    public static final String INSUFFICIENT_BALANCE= "Saldo insuficiente";
    public static final String TRANSACTION_TIME_LIMIT_EXCEEDED_MESSAGE = "La transaccion supera el limite de tiempo de 24 horas";
    public static final String SUCESS_STATUS = "anulada";
    public static final String ANULATE_STATUS = "anulada";


}
