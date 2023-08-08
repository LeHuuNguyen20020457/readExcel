package com.iplat.master.file;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Configuration;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class VelocityWriter {

    private ReadExcel readExcel;

    public VelocityWriter() {}

    public VelocityWriter(ReadExcel readExcel) {
        this.readExcel = readExcel;
    }


    public ReadExcel getReadExcel() {
        return readExcel;
    }

    public void setReadExcel(ReadExcel readExcel) {
        this.readExcel = readExcel;
    }

    public void velocityReadFileGenEntity () throws IOException {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("vtemplates/class.vm");

        VelocityContext context = new VelocityContext();

        List<SheetEntity> sheetEntityList = new ArrayList<>();

       try {
           sheetEntityList  = this.readExcel.readFileExcel();
       }
       catch (IOException e) {
           System.out.println("read file error");
       }

       System.out.println(sheetEntityList);

       // read file and writer entity

        for(SheetEntity sheet : sheetEntityList) {

            context.put("packageName", this.readExcel.getValueCell(sheet.getSheetNumber(), sheet.getLocation().getRowIndex(), sheet.getLocation().getColumnIndex()));


            for(TableEntity table : sheet.getTableEntities()) {
                String outputPathEntity = "";
                String className = "";
                if(table.getType() == TypeTable.OBJECT_PARAMETER) {
                    className = this.readExcel.getValueCell(sheet.getSheetNumber(), sheet.getInput().getRowIndex(), sheet.getInput().getColumnIndex());
                    context.put("className", className);
                }
                if(table.getType() == TypeTable.SAMPLE){
                    className = this.readExcel.getValueCell(sheet.getSheetNumber(), sheet.getExport().getRowIndex(), sheet.getExport().getColumnIndex());
                    context.put("className", className);
                }
                if(table.getType() == TypeTable.PARAMETER || table.getType() == TypeTable.SAMPLE_ENTITY) {
                    continue;
                }

                Coordinate location = sheet.getLocation();

                String path = this.readExcel.getValueCell(sheet.getSheetNumber(), location.getRowIndex(), location.getColumnIndex()).replace('.', '\\');

                outputPathEntity = "src\\main\\java\\" + path + "\\" + className  + ".java";
                context.put("properties", table.getFieldSamples());

                StringWriter writer = new StringWriter();

                t.merge(context, writer);

                if(!Files.exists(Path.of(outputPathEntity))) {
                    try {
                        FileWriter writerEntity = new FileWriter(new File(outputPathEntity));
                        writerEntity.write(writer.toString());
                        writerEntity.close();
                        System.out.println("Class entity generated successfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void velocityReadFileGenMapper () {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        Template tMapper = velocityEngine.getTemplate("vtemplates/sampleMapper.vm");

        VelocityContext context = new VelocityContext();

        StringWriter writer = new StringWriter();


        tMapper.merge(context, writer);
        String outputPathMapper = "src\\main\\java\\com\\iplat\\master\\ddd\\infrastructure\\sample\\mapper\\SampleMapper.java";

        if(!Files.exists(Path.of(outputPathMapper))) {
            try {
                FileWriter writerMapper = new FileWriter(new File(outputPathMapper));
                writerMapper.write(writer.toString());
                writerMapper.close();
                System.out.println("Mapper generated successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToFile(String content, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
