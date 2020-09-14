package com.agile;

import static org.junit.Assert.assertTrue;

import com.agile.parquet.sql.ExecuteSql;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ParquetTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        ExecuteSql executeSql = new ExecuteSql.ExecuteSqlConf().setSqlContent("select * from demo01;")
                .setParquetFilePath("D:/data/demo.parquet").setTempTableName("demo01").build();
        String data = executeSql.execute();
        executeSql.show(data);
    }
}
