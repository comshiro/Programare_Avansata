import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.awt.Desktop;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportCommand implements Command {
    private final Repository repo;

    public ReportCommand(Repository repo) {
        this.repo = repo;
    }

    @Override
    public String getCommandName() {
        return "report";
    }

    @Override
    public List<String> getOptions() {
        return List.of();
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public int execute() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setDirectoryForTemplateLoading(new File("resources/templates"));
            cfg.setDefaultEncoding("UTF-8");

            // Load template
            Template template = cfg.getTemplate("report.ftl");

            // Prepare data
            Map<String, Object> data = new HashMap<>();
            data.put("images", repo.getImages());

            // Generate HTML file
            File output = new File("report.html");
            try (Writer writer = new FileWriter(output)) {
                template.process(data, writer);
            }

            System.out.println("Report generated: " + output.getAbsolutePath());

            //Open in browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(output.toURI());
            }

            return 0;

        } catch (IOException | TemplateException e) {
            System.out.println("Error generating report: " + e.getMessage());
            return -1;
        }
    }
}
