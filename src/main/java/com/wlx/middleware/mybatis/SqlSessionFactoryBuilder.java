package com.wlx.middleware.mybatis;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSessionFactoryBuilder {

    public DefaultSqlSessionFactory build(Reader reader) {

        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            Element rootElement = document.getRootElement();
            Configuration configuration = parseConfiguration(rootElement);
            return new DefaultSqlSessionFactory(configuration);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private Configuration parseConfiguration(Element rootElement) {
        Configuration configuration = new Configuration();

        // dataSource
        Map<String, String> dataSourceMap = new HashMap<>();
        List<Element> dataSources = rootElement.selectNodes("//dataSource");
        Element element = dataSources.get(0);
        List<Element> contents = element.content();
        for (Element content : contents) {
            String name = content.attributeValue("name");
            String value = content.attributeValue("value");
            dataSourceMap.put(name, value);
        }
        configuration.setDataSource(dataSourceMap);

        // connection
        try {
            Class.forName(dataSourceMap.get("driver"));
            Connection connection = DriverManager.getConnection(dataSourceMap.get("url"),
                    dataSourceMap.get("username"), dataSourceMap.get("password"));
            configuration.setConnection(connection);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        // mapperElement
        Map<String, XNode> mapperElement = parseMapperElement(rootElement.selectNodes("mappers"));
        configuration.setMapperElement(mapperElement);
        return configuration;
    }


    private Map<String, XNode> parseMapperElement(List<Element> elements) {
        Map<String, XNode> mapperElement = new HashMap<>();

        Element firstMapper = elements.get(0);
        for (Object obj : firstMapper.content()) {
            Element element = (Element) obj;
            String resource = element.attributeValue("resource");
            Reader reader = null;
            try {
                reader = Resources.getResourceAsReader(resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(new InputSource(reader));
                Element rootElement = document.getRootElement();
                String namespace = rootElement.attributeValue("namespace");

                List<Element> selectNodes = rootElement.selectNodes("select");
                for (Element selectNode : selectNodes) {
                    String id = selectNode.attributeValue("id");
                    String parameterType = selectNode.attributeValue("parameterType");
                    String resultType = selectNode.attributeValue("resultType");
                    String sql = selectNode.getText();

                    Map<Integer, String> parameterMap = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#(\\{.*?}))");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String group1 = matcher.group(1);
                        String group2 = matcher.group(2);
                        parameterMap.put(i, group2);
                        sql = sql.replace(group1, "?");
                    }

                    XNode xNode = new XNode(namespace, id, parameterType, resultType, sql, parameterMap);
                    mapperElement.put(namespace + "." + id, xNode);
                }
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }
        return mapperElement;
    }

}
