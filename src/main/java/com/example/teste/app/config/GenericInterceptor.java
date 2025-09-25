//package com.example.teste.app.config;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class GenericInterceptor {
//
//    private final ElasticIndexer elasticIndexer;
//
//    public GenericInterceptor(ElasticIndexer elasticIndexer) {
//        this.elasticIndexer = elasticIndexer;
//    }
//
//    @Around("within(com.example.teste.app..*)")
//    public Object interceptAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
//        // Executa o método original
//
//        Object result = joinPoint.proceed();
//
//        // Se o retorno não for nulo, indexa no Elasticsearch
//        if (result != null) {
//            // Use a fixed index name "httplogs" for all logs
//            elasticIndexer.index(result, "httplogs");
//        }
//
//        return result;
//    }
//}
