/**
 * This class is generated by jOOQ
 */
package sample.model.ticket.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.1"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tickets extends org.jooq.impl.TableImpl<sample.model.ticket.tables.records.TicketsRecord> {

	private static final long serialVersionUID = -1489170935;

	/**
	 * The reference instance of <code>ticket.tickets</code>
	 */
	public static final sample.model.ticket.tables.Tickets TICKETS = new sample.model.ticket.tables.Tickets();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<sample.model.ticket.tables.records.TicketsRecord> getRecordType() {
		return sample.model.ticket.tables.records.TicketsRecord.class;
	}

	/**
	 * The column <code>ticket.tickets.ticket_code</code>.
	 */
	public final org.jooq.TableField<sample.model.ticket.tables.records.TicketsRecord, java.lang.String> TICKET_CODE = createField("ticket_code", org.jooq.impl.SQLDataType.VARCHAR.length(10).nullable(false), this, "");

	/**
	 * The column <code>ticket.tickets.total_limit</code>.
	 */
	public final org.jooq.TableField<sample.model.ticket.tables.records.TicketsRecord, java.lang.Integer> TOTAL_LIMIT = createField("total_limit", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>ticket.tickets.users_limit</code>.
	 */
	public final org.jooq.TableField<sample.model.ticket.tables.records.TicketsRecord, java.lang.Integer> USERS_LIMIT = createField("users_limit", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>ticket.tickets</code> table reference
	 */
	public Tickets() {
		this("tickets", null);
	}

	/**
	 * Create an aliased <code>ticket.tickets</code> table reference
	 */
	public Tickets(java.lang.String alias) {
		this(alias, sample.model.ticket.tables.Tickets.TICKETS);
	}

	private Tickets(java.lang.String alias, org.jooq.Table<sample.model.ticket.tables.records.TicketsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Tickets(java.lang.String alias, org.jooq.Table<sample.model.ticket.tables.records.TicketsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, sample.model.ticket.Ticket.TICKET, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<sample.model.ticket.tables.records.TicketsRecord> getPrimaryKey() {
		return sample.model.ticket.Keys.KEY_TICKETS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<sample.model.ticket.tables.records.TicketsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<sample.model.ticket.tables.records.TicketsRecord>>asList(sample.model.ticket.Keys.KEY_TICKETS_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public sample.model.ticket.tables.Tickets as(java.lang.String alias) {
		return new sample.model.ticket.tables.Tickets(alias, this);
	}

	/**
	 * Rename this table
	 */
	public sample.model.ticket.tables.Tickets rename(java.lang.String name) {
		return new sample.model.ticket.tables.Tickets(name, null);
	}
}
