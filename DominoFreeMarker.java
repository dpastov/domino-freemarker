package metz.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.NotesException;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class DominoFreemarker {
	private Configuration cfg;

	public DominoFreemarker(Database database) throws NotesException {
		cfg = new Configuration(Configuration.VERSION_2_3_26);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);

		// Get the template (uses cache internally)
		DominoTemplateLoader dominoLoader = new DominoTemplateLoader(database);
		cfg.setTemplateLoader(dominoLoader);
	}

	public String apply(String templateId, Map<String, Object> items) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		/* Initialize main template */
		Template template = cfg.getTemplate(templateId);

		/* Merge data-model with template */
		Writer out = new StringWriter();
		template.process(items, out);

		return out.toString();
	}	
}