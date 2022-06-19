package com.hapinistay.backend.oauth;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.descriptor.sql.LongVarcharTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class PGSQLMapDialect extends PostgreSQL94Dialect {


	  @Override
	  public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
	    if (Types.BLOB == sqlTypeDescriptor.getSqlType()) {
	      return org.hibernate.type.descriptor.sql.BinaryTypeDescriptor.INSTANCE;
	    		  //BinaryTypeDescriptor.INSTANCE;
	    		  //LongVarcharTypeDescriptor.INSTANCE;
	    }
	    return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
	  }


	}