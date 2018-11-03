package me.saro.sap.jco;

/**
 * Sap Manager Builder Option
 * @author      PARK Yong Seo
 * @since       0.1
 * @see
 * com.sap.conn.jco.ext.DestinationDataProvider
 */
public enum SapManagerBuilderOption {

    AUTH_TYPE("jco.destination.auth_type"),

    AUTH_TYPE_CONFIGURED_USER("CONFIGURED_USER"),

    AUTH_TYPE_CURRENT_USER("CURRENT_USER"),

    CLIENT("jco.client.client"),

    USER("jco.client.user"),

    ALIAS_USER("jco.client.alias_user"),

    PASSWD("jco.client.passwd"),

    LANG("jco.client.lang"),

    CODEPAGE("jco.client.codepage"),

    PCS("jco.client.pcs"),

    ASHOST("jco.client.ashost"),

    SYSNR("jco.client.sysnr"),

    MSHOST("jco.client.mshost"),

    MSSERV("jco.client.msserv"),

    R3NAME("jco.client.r3name"),

    GROUP("jco.client.group"),

    SAPROUTER("jco.client.saprouter"),

    MYSAPSSO2("jco.client.mysapsso2"),

    GETSSO2("jco.client.getsso2"),

    X509CERT("jco.client.x509cert"),

    EXTID_DATA("jco.client.extid_data"),

    EXTID_TYPE("jco.client.extid_type"),

    LCHECK("jco.client.lcheck"),

    DELTA("jco.client.delta"),

    SNC_PARTNERNAME("jco.client.snc_partnername"),

    SNC_QOP("jco.client.snc_qop"),

    SNC_MYNAME("jco.client.snc_myname"),

    SNC_MODE("jco.client.snc_mode"),

    SNC_SSO("jco.client.snc_sso"),

    SNC_LIBRARY("jco.client.snc_lib"),

    DEST("jco.client.dest"),

    PEAK_LIMIT("jco.destination.peak_limit"),

    POOL_CAPACITY("jco.destination.pool_capacity"),

    EXPIRATION_TIME("jco.destination.expiration_time"),

    EXPIRATION_PERIOD("jco.destination.expiration_check_period"),

    MAX_GET_TIME("jco.destination.max_get_client_time"),

    REPOSITORY_DEST("jco.destination.repository_destination"),

    REPOSITORY_USER("jco.destination.repository.user"),

    REPOSITORY_PASSWD("jco.destination.repository.passwd"),

    REPOSITORY_SNC("jco.destination.repository.snc_mode"),

    CPIC_TRACE("jco.client.cpic_trace"),

    TRACE("jco.client.trace"),

    GWHOST("jco.client.gwhost"),

    GWSERV("jco.client.gwserv"),

    TPHOST("jco.client.tphost"),

    TPNAME("jco.client.tpname"),

    TYPE("jco.client.type"),

    USE_SAPGUI("jco.client.use_sapgui"),

    DENY_INITIAL_PASSWORD("jco.client.deny_initial_password");

    private String value;

    private SapManagerBuilderOption(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
