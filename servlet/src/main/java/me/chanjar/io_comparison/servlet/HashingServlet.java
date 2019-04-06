package me.chanjar.io_comparison.servlet;

import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "hashServlet", urlPatterns = { "/hasher" }, loadOnStartup = 1)
public class HashingServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
      ServletException, IOException {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //ignore
    }
    String id = req.getParameter("id");
    String pathToFile = getFileBasedOnId(id);
    try (FileInputStream fileInputStream = new FileInputStream(new File(pathToFile));
        PrintWriter writer = resp.getWriter()) {
      String sha512Hex = DigestUtils.sha512Hex(fileInputStream);
      resp.setContentType("text/plain");
      writer.println(sha512Hex);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private String getFileBasedOnId(String id) {
    return "/Users/qianjia/tmp/reactor-test/img-" + id + ".png";
  }

}
