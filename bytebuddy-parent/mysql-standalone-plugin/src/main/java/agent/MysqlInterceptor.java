package agent;

import com.alibaba.fastjson2.JSON;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;


/**
 * @Description:
 * @Date : 2023/03/05 13:59
 * @Auther : tiankun
 */
@Slf4j
public class MysqlInterceptor {

    @RuntimeType
    public Object intercept(
            @This Object targetObj,
            @Origin Method targetMethod,
            @AllArguments Object[] targetMethodArgs,
            @SuperCall Callable<?> zuper
    ) {
        long start = System.currentTimeMillis();
        Object call = null;
        try {
            log.info("targetObj :{}", targetObj);
            ClientPreparedStatement clientPreparedStatement = (ClientPreparedStatement) targetObj;
            log.info("preparedSql :{}", clientPreparedStatement.getPreparedSql());
            log.info("current database :{}",clientPreparedStatement.getCurrentDatabase());
            log.info("executeTime :{}", clientPreparedStatement.getExecuteTime() + "");
            log.info("maxRows: {}", clientPreparedStatement.getMaxRows() + "");
            log.info("queryBiddings: {}" , JSON.toJSONString(clientPreparedStatement.getQueryBindings()));
            log.info("before mysql exec,methodName:{},args:{}",targetMethod.getName(), Arrays.toString(targetMethodArgs));

            call = zuper.call();
            log.info("after mysql exec,result:{}",call);
        }catch (Exception e) {
            log.error("mysql exec error",e);
        }finally {
            long end = System.currentTimeMillis();
            log.info("finally mysql exec in {} ms",end - start);
        }
        return call;
    }
}
