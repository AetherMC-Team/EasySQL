package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractSQLAction<T> implements SQLAction<T> {

    private final @NotNull SQLManagerImpl sqlManager;

    private final @NotNull UUID uuid;
    private final long createTime;

    protected @NotNull String sqlContent;

    protected @Nullable BiConsumer<SQLException, SQLAction<T>> exceptionHandler = null;

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        this(manager, sql, System.currentTimeMillis());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, @NotNull UUID uuid) {
        this(manager, sql, uuid, System.currentTimeMillis());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, long createTime) {
        this(manager, sql, UUID.randomUUID(), createTime);
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql,
                             @NotNull UUID uuid, long createTime) {
        this.sqlManager = manager;
        this.sqlContent = sql;
        this.uuid = uuid;
        this.createTime = createTime;
    }


    @Override
    public @NotNull UUID getActionUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull String getShortID() {
        return getActionUUID().toString().substring(0, 8);
    }

    @Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public @NotNull String getSQLContent() {
        return this.sqlContent.trim();
    }

    @Override
    public @NotNull SQLManagerImpl getManager() {
        return this.sqlManager;
    }

    protected void outputDebugMessage() {
        getManager().debug("#" + getShortID() + " ->" + getSQLContent());
    }

    public void handleException(SQLException exception) {
        if (this.exceptionHandler == null) {
            defaultExceptionHandler().accept(exception, this);
        } else {
            this.exceptionHandler.accept(exception, this);
        }
    }

    @Override
    public void executeAsync(Consumer<T> success, BiConsumer<SQLException, SQLAction<T>> failure) {
        getManager().getExecutorPool().submit(() -> {
            try {
                T returnedValue = execute();
                if (success != null) success.accept(returnedValue);
            } catch (SQLException e) {
                (failure == null ? defaultExceptionHandler() : failure).accept(e, this);
            }
        });
    }


}
