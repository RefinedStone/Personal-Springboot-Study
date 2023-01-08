package com.example.PersonalSpringStudy.test.list_param;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QParamTest is a Querydsl query type for ParamTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParamTest extends EntityPathBase<ParamTest> {

    private static final long serialVersionUID = 1410826326L;

    public static final QParamTest paramTest = new QParamTest("paramTest");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QParamTest(String variable) {
        super(ParamTest.class, forVariable(variable));
    }

    public QParamTest(Path<? extends ParamTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParamTest(PathMetadata metadata) {
        super(ParamTest.class, metadata);
    }

}

