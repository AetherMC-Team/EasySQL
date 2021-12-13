package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.builder.ReplaceBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public abstract class ReplaceBuilderImpl<T> extends AbstractSQLBuilder implements ReplaceBuilder<T> {

	String tableName;

	public ReplaceBuilderImpl(@NotNull SQLManagerImpl manager, String tableName) {
		super(manager);
		this.tableName = tableName;
	}

	protected static String buildSQL(String tableName, List<String> columnNames) {
		int valueLength = columnNames.size();
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("REPLACE INTO `").append(tableName).append("`(");
		Iterator<String> iterator = columnNames.iterator();
		while (iterator.hasNext()) {
			sqlBuilder.append("`").append(iterator.next()).append("`");
			if (iterator.hasNext()) sqlBuilder.append(", ");
		}

		sqlBuilder.append(") VALUES (");

		for (int i = 0; i < valueLength; i++) {
			sqlBuilder.append("?");
			if (i != valueLength - 1) {
				sqlBuilder.append(", ");
			}
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}

	public String getTableName() {
		return tableName;
	}

	public ReplaceBuilderImpl<T> setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
}
