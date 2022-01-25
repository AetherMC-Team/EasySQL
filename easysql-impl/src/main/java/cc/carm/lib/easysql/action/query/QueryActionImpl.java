package cc.carm.lib.easysql.action.query;

import cc.carm.lib.easysql.action.AbstractSQLAction;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLHandler;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.query.SQLQueryImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryActionImpl extends AbstractSQLAction<SQLQuery> implements QueryAction {

	public QueryActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
	}

	@Override
	public @NotNull SQLQueryImpl execute() throws SQLException {

		Connection connection = getManager().getConnection();
		Statement statement = connection.createStatement();

		outputDebugMessage();

		long executeTime = System.currentTimeMillis();
		ResultSet resultSet = statement.executeQuery(getSQLContent());
		SQLQueryImpl query = new SQLQueryImpl(
				getManager(), this,
				connection, statement, resultSet,
				executeTime
		);

		getManager().getActiveQuery().put(getActionUUID(), query);

		return query;
	}


	@Override
	public void executeAsync(SQLHandler<SQLQuery> success, SQLExceptionHandler failure) {
		try (SQLQueryImpl query = execute()) {
			if (success != null) success.accept(query);
		} catch (SQLException exception) {
			handleException(failure, exception);
		}
	}
}
