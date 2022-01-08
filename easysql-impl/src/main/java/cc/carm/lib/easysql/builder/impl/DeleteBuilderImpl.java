package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.PreparedSQLUpdateActionImpl;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.builder.DeleteBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

public class DeleteBuilderImpl
        extends AbstractConditionalBuilder<DeleteBuilder, PreparedSQLUpdateAction>
        implements DeleteBuilder {

    String tableName;

    public DeleteBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
    }

    @Override
    public PreparedSQLUpdateAction build() {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("DELETE FROM `").append(getTableName()).append("`");

        if (hasConditions()) sqlBuilder.append(" ").append(buildConditionSQL());
        if (limit > 0) sqlBuilder.append(" ").append(buildLimitSQL());

        return new PreparedSQLUpdateActionImpl(
                getManager(), sqlBuilder.toString(),
                hasConditionParams() ? getConditionParams() : null
        );
    }

    @Override
    public String getTableName() {
        return tableName;
    }


    @Override
    protected DeleteBuilderImpl getThis() {
        return this;
    }

}
