package com.pivotal.hawq.mapreduce.ft;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import com.google.common.collect.Lists;
import com.pivotal.hawq.mapreduce.DataProvider;
import com.pivotal.hawq.mapreduce.HAWQTable;
import com.pivotal.hawq.mapreduce.SimpleTableClusterTester;
import com.pivotal.hawq.mapreduce.metadata.HAWQTableFormat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Miscellaneous tests for AO tables.
 */
public class HAWQInputFormatFeatureTest_AO_Misc extends SimpleTableClusterTester {

	@BeforeClass
	public static void setUpBeforeClass() {
		System.out.println("Executing test suite: AO_Misc");
	}

	@Test
	public void testAOLargeContent() throws Exception {
		HAWQTable table = new HAWQTable.Builder("test_ao_largecontent", Lists.newArrayList("text"))
				.storage(HAWQTableFormat.AO)
				.blockSize(32768)    // 32K
				.provider(new DataProvider() {
					@Override
					public String getInsertSQLs(HAWQTable table) {
						return "INSERT INTO " + table.getTableName() + " values (repeat('b', 40000));";
					}
				}).build();

		testSimpleTable(table);
	}

	@Test
	public void testAOEmptyTable() throws Exception {
		HAWQTable table = new HAWQTable.Builder("test_ao_empty", Lists.newArrayList("int4"))
				.storage(HAWQTableFormat.AO)
				.provider(DataProvider.EMPTY)
				.build();

		testSimpleTable(table);
	}

	@Test
	public void testAORecordGetAllTypes() throws Exception {
		HAWQTable table = new HAWQTable.Builder("test_ao_alltypes", FeatureTestAllTypesMapper.types)
				.storage(HAWQTableFormat.AO)
				.provider(DataProvider.RANDOM)
				.build();

		testSimpleTable(table, FeatureTestAllTypesMapper.class);
	}
}
