package dbconverter.odoo;

public interface Field {
	public String n();

	public enum FemsDevice implements Field {
		ID("id"), //
		APIKEY("apikey"), //
		NAME("name"), //
		COMMENT("comment"), //
		STATE("state"), //
		OPENEMS_VERSION("openems_version"), //
		PRODUCT_TYPE("producttype"), //
		OPENEMS_CONFIG("openems_config"), //
		LAST_MESSAGE("lastmessage"), //
		LAST_UPDATE("lastupdate"), //
		SOC("soc"), //
		IPV4("ipv4");

		private final String n;

		private FemsDevice(String n) {
			this.n = n;
		}

		public String n() {
			return n;
		}
	}
}
