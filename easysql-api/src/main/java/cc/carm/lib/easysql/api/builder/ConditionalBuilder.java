package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.SQLBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.LinkedHashMap;

public interface ConditionalBuilder<B extends ConditionalBuilder<B, T>, T extends SQLAction<?>> extends SQLBuilder {

	/**
	 * 将现有条件构建完整的SQL语句用于执行。
	 *
	 * @return {@link cc.carm.lib.easysql.api.SQLAction}
	 */
	T build();

	/**
	 * 设定取出的条数
	 *
	 * @param limit 条数限制
	 * @return {@link B}
	 */
	B setLimit(int limit);

	B setConditions(@Nullable String condition);

	B setConditions(LinkedHashMap<@NotNull String, @Nullable Object> conditionSQLs);

	B addCondition(@Nullable String condition);

	B addCondition(@NotNull String queryName, @NotNull String operator, @Nullable Object queryValue);

	default B addCondition(@NotNull String queryName, @Nullable Object queryValue) {
		return addCondition(queryName, "=", queryValue);
	}

	B addCondition(@NotNull String[] queryNames, @Nullable Object[] queryValues);

	B addNotNullCondition(@NotNull String queryName);

	default B addTimeCondition(@NotNull String queryName, long startMillis, long endMillis) {
		return addTimeCondition(queryName,
				startMillis > 0 ? new Date(startMillis) : null,
				endMillis > 0 ? new Date(endMillis) : null
		);
	}

	B addTimeCondition(@NotNull String queryName, @Nullable java.util.Date startDate, @Nullable java.util.Date endDate);


}
