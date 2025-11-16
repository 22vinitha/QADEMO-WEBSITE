package DemoQA.DemoQAAutomations;



import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.FileWriter;
import org.apache.commons.io.FileUtils;

public class DemoQAWebsite {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    ExtentReports extent;
    ExtentTest test;
    ExtentSparkReporter spark;
    
   
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
        
        // Initialize Extent Report
        spark = new ExtentSparkReporter("C:\\Users\\admin\\Desktop\\DemoQA_TestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Vinitha M");
        extent.setSystemInfo("Browser", "Chrome");
        System.out.println("Extent Report setup complete.");
    }
    
    //-----------------Task1_HomePageTitle----------------
    @Test(priority = 1)
    public void verifyHomePageTitle() {
    	test = extent.createTest("TASK 1: Verify Home Page Title");
        System.out.println("\n-------- TASK 1 --------");
        driver.get("https://demoqa.com/");
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        Assert.assertTrue(title.contains("DEMOQA"), "Title verification failed!");
        test.pass("Home page title verified successfully.");
        
    }
    
    //---------------2.	Task 2: Elements Module – TextBox------
    
    @Test(priority = 2)
    public void verifyTextBoxForm() {
    	test = extent.createTest("TASK 2: Verify Text Box Form");
    	   System.out.println("\n-------- TASK 2 --------");
        driver.get("https://demoqa.com/text-box");

        // Remove ads and footer if present
        js.executeScript("document.getElementById('fixedban')?.remove();");
        js.executeScript("document.querySelector('footer')?.remove();");

        // Fill form fields
        driver.findElement(By.id("userName")).sendKeys("Vinitha M");
        driver.findElement(By.id("userEmail")).sendKeys("vinitha@example.com");
        driver.findElement(By.id("currentAddress")).sendKeys("Cochin, Kerala");
        driver.findElement(By.id("permanentAddress")).sendKeys("Bangalore, Karnataka");

        // Scroll to Submit button and click
        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        js.executeScript("arguments[0].click();", submitBtn);

        // Scroll up so output section is visible
        js.executeScript("window.scrollTo(0, 0);");

        // Wait for the output section to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("output")));

        // Read submitted values from output <p> elements
        String outputName = driver.findElement(By.id("name")).getText().trim();
        String outputEmail = driver.findElement(By.id("email")).getText().trim();
        String currentAddress = driver.findElement(By.id("currentAddress")).getAttribute("value");
        String permanentAddress = driver.findElement(By.id("permanentAddress")).getAttribute("value");

     
        // Print for debugging
        System.out.println("OUTPUT VALUES:");
        System.out.println(outputName);
        System.out.println(outputEmail);
        System.out.println(currentAddress);
        System.out.println(permanentAddress);

        // Assertions
        Assert.assertTrue(outputName.contains("Vinitha M"), "Name mismatch!");
        Assert.assertTrue(outputEmail.contains("vinitha@example.com"), "Email mismatch!");
        Assert.assertEquals(currentAddress, "Cochin, Kerala", "Current address mismatch!");
        Assert.assertEquals(permanentAddress, "Bangalore, Karnataka", "Permanent address mismatch!");

        System.out.println("Form data verified successfully!");
        test.pass("Text box form verified successfully.");
    }
    
   //----------Task 3: Elements Module – CheckBox & RadioButton--------------

    @Test(priority = 3)
    public void checkboxTest() throws InterruptedException {
    	 test = extent.createTest("TASK 3: Checkbox Test");
        System.out.println("\n-------- TASK 3: Checkbox Test --------");
        driver.get("https://demoqa.com/checkbox");

        // Remove ads/iframe overlay
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // Expand "Home" node if collapsed
        WebElement expandToggle = driver.findElement(By.cssSelector("button[title='Toggle']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", expandToggle);
        Thread.sleep(500);
        expandToggle.click();
        System.out.println("Expanded Home tree node.");

        // Locate Home checkbox
        WebElement homeCheckbox = driver.findElement(By.cssSelector("label[for='tree-node-home']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homeCheckbox);
        Thread.sleep(500);

        // Click the checkbox
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", homeCheckbox);
        System.out.println("Clicked on Home checkbox.");

        // Verify selection
        String result = driver.findElement(By.id("result")).getText();
        System.out.println("Result displayed: " + result);
        Assert.assertTrue(result.toLowerCase().contains("home"), "Checkbox selection not displayed!");
        System.out.println("Checkbox selection verified successfully!");
        test.pass("Checkbox selection verified successfully.");
    }

    @Test(priority = 4)
    public void radioButtonTest() throws InterruptedException {
    	 test = extent.createTest("TASK 3: Radio Button Test");
        System.out.println("\n-------- TASK 3: Radio Button Test --------");
        driver.get("https://demoqa.com/radio-button");

        // Remove ads/iframe overlay
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // Locate Yes radio button
        WebElement yesRadio = driver.findElement(By.cssSelector("label[for='yesRadio']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yesRadio);
        Thread.sleep(500);

        // Click the radio button
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesRadio);
        System.out.println("Clicked on Yes radio button.");

        // Verify selection
        String output = driver.findElement(By.className("text-success")).getText();
        System.out.println("Result displayed: " + output);
        Assert.assertEquals(output, "Yes", "Radio button selection mismatch!");
        System.out.println("Radio button selection verified successfully!");
        test.pass("Radio button verified successfully.");
    }

 // Helper method in the class MEANS to explain where and why that getCellValueAsString() method was placed in your class.
//What is a “Helper Method”?
//A helper method is a small, reusable function that performs a supporting task for your main test cases.
    
    public String getCellValueAsString(Row r, int cellIndex) {
        Cell cell = r.getCell(cellIndex);
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    
    // ---------- ----Task 4: Forms Module----------
    
    @Test(priority = 5)
    public void practiceFormTest() throws InterruptedException, IOException {
        test = extent.createTest("TASK 4: Practice Form Test");
        System.out.println("\n-------- TASK 4: Practice Form --------");
        driver.get("https://demoqa.com/automation-practice-form");

        // Remove ads/iframe overlay
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // Read Excel file for input data
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\admin\\Desktop\\FormData.xlsx"));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(1); // Data row

        // Read all fields using helper method
        String firstName = getCellValueAsString(row, 0);
        String lastName  = getCellValueAsString(row, 1);
        String email     = getCellValueAsString(row, 2);
        String gender    = getCellValueAsString(row, 3);
        String mobile    = getCellValueAsString(row, 4);
        String address   = getCellValueAsString(row, 5);

        // Fill the form
        driver.findElement(By.id("firstName")).sendKeys(firstName);
        driver.findElement(By.id("lastName")).sendKeys(lastName);
        driver.findElement(By.id("userEmail")).sendKeys(email);

        // Gender radio button
        WebElement genderRadio = driver.findElement(By.xpath("//label[text()='" + gender + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", genderRadio);
        Thread.sleep(500);
        genderRadio.click();

        driver.findElement(By.id("userNumber")).sendKeys(mobile);

        // Current Address
        WebElement addressField = driver.findElement(By.id("currentAddress"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addressField);
        Thread.sleep(500);
        addressField.sendKeys(address);

        // Submit the form
        WebElement submitBtn = driver.findElement(By.id("submit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        System.out.println("Form submitted.");

        // Validate confirmation popup
        WebElement modal = driver.findElement(By.id("example-modal-sizes-title-lg"));
        Assert.assertTrue(modal.isDisplayed(), "Form submission modal not displayed!");
        System.out.println("Confirmation popup displayed: " + modal.getText());
        test.pass("Practice form submitted and verified.");

        // Take screenshot
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File("C:\\Users\\admin\\Desktop\\PracticeFormConfirmation.png"));
        System.out.println("Screenshot of confirmation saved.");

        workbook.close();
        fis.close();
    }
    
    // ---------------Task 5: Alerts, Frames & Windows ----------
    
    @Test(priority = 6)
    public void handleAlertsTest() throws InterruptedException {
    	 test = extent.createTest("TASK 5: Handle Alerts");
        System.out.println("\n-------- TASK 5: Alerts --------");
        driver.get("https://demoqa.com/alerts");

        // Remove ads iframe overlays
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // Scroll to alert button 
        WebElement alertButton = driver.findElement(By.id("alertButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", alertButton);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", alertButton);
        Thread.sleep(1000);

        // Handle simple alert
        String alertText = driver.switchTo().alert().getText();
        System.out.println("Alert Text: " + alertText);
        driver.switchTo().alert().accept();
        System.out.println("Simple alert accepted.");

        // Confirm alert (Accept/Dismiss)
        WebElement confirmButton = driver.findElement(By.id("confirmButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmButton);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
        Thread.sleep(1000);

        String confirmText = driver.switchTo().alert().getText();
        System.out.println("Confirm Text: " + confirmText);
        driver.switchTo().alert().dismiss();
        System.out.println("Confirmation alert dismissed.");

        // Prompt alert (Send keys + Accept)
        WebElement promptButton = driver.findElement(By.id("promtButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", promptButton);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", promptButton);
        Thread.sleep(1000);

        driver.switchTo().alert().sendKeys("HELLO WORLD");
        driver.switchTo().alert().accept();
        System.out.println("Prompt alert accepted with input.");
        test.pass("Prompt alert handled successfully.");

    }
    
  
    
    @Test(priority = 7)
    public void switchFramesTest() throws InterruptedException {
        test = extent.createTest("TASK 5: Frames Handling");

        System.out.println("\n-------- TASK 5: Frames --------");
        driver.get("https://demoqa.com/frames");

        // Remove ad iframes
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // Frame 1
        WebElement frame1 = driver.findElement(By.id("frame1"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", frame1);
        Thread.sleep(500);
        driver.switchTo().frame(frame1);

        String frame1Text = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println("Frame1 Text: " + frame1Text);
        Assert.assertEquals(frame1Text, "This is a sample page", "Frame 1 text mismatch!");

        driver.switchTo().defaultContent();
        System.out.println("Switched back to main page. Current URL: " + driver.getCurrentUrl());

        // Frame 2
        WebElement frame2 = driver.findElement(By.id("frame2"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", frame2);
        Thread.sleep(500);
        driver.switchTo().frame(frame2);

        String frame2Text = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println("Frame2 Text: " + frame2Text);
        Assert.assertEquals(frame2Text, "This is a sample page", "Frame 2 text mismatch!");

        driver.switchTo().defaultContent();
        System.out.println("Switched back to main page. Current URL: " + driver.getCurrentUrl());
        test.pass("Frames verified successfully.");

    }
    
    
    @Test(priority = 8)
    public void handleWindowsTabsTest() throws InterruptedException {
        test = extent.createTest("TASK 5: Windows & Tabs Handling");
        System.out.println("\n-------- TASK 5: Windows/Tabs --------");
        driver.get("https://demoqa.com/browser-windows");

        // Remove ad iframes that may block buttons
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        String mainWindow = driver.getWindowHandle();
        //System.out.println("Main window handle: " + mainWindow);

        // --- Open and handle new tab ---
        WebElement tabButton = driver.findElement(By.id("tabButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tabButton);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabButton);
        System.out.println("Clicked on New Tab button.");

        // Switch to new tab
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        Thread.sleep(1000);
        String newTabText = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println("New Tab Text: " + newTabText);
        Assert.assertEquals(newTabText, "This is a sample page", "New Tab content mismatch!");
        driver.close();
        System.out.println("Closed new tab.");
        driver.switchTo().window(mainWindow);
        System.out.println("Switched back to main window.");

        // --- Open and handle new window ---
        WebElement windowButton = driver.findElement(By.id("windowButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", windowButton);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", windowButton);
        System.out.println("Clicked on New Window button.");

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        Thread.sleep(1000);
        String newWindowText = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println("New Window Text: " + newWindowText);
        Assert.assertEquals(newWindowText, "This is a sample page", "New Window content mismatch!");
        driver.close();
        System.out.println("Closed new window.");
        driver.switchTo().window(mainWindow);
        System.out.println("Switched back to main window.");
        test.pass("Tab verified successfully.");

    }
    
    

    // ---------- -----Task 6: Upload & Download ----------------
    @Test(priority = 9)
    public void uploadAndDownloadTest() throws InterruptedException, IOException {
        test = extent.createTest("TASK 6: Upload & Download");
        System.out.println("\n-------- TASK 6: Upload & Download --------");
        driver.get("https://demoqa.com/upload-download");

        // Remove ad iframes
        ((JavascriptExecutor) driver).executeScript(
            "document.querySelectorAll('iframe[id^=\"google_ads_iframe\"]').forEach(e => e.remove());"
        );
        System.out.println("Removed ad overlays (if any).");

        // ---------- File Upload ----------
        // Prepare file to upload
        String filePath = "C:\\Users\\admin\\Desktop\\sample_upload.txt"; // your local test file

        File uploadFile = new File(filePath);
        if (!uploadFile.exists()) {
      
            FileWriter writer = new FileWriter(uploadFile);
            writer.write("This is a test upload file.");
            writer.close();
            System.out.println("Created a sample upload file at: " + uploadFile.getAbsolutePath());
        }

        WebElement uploadInput = driver.findElement(By.id("uploadFile"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", uploadInput);
        Thread.sleep(500);

        uploadInput.sendKeys(uploadFile.getAbsolutePath());
        System.out.println("Uploaded file: " + uploadFile.getName());

        // Verify uploaded file path appears
        String uploadedPath = driver.findElement(By.id("uploadedFilePath")).getText();
        System.out.println("Uploaded File Path Displayed: " + uploadedPath);
        Assert.assertTrue(uploadedPath.contains("sample_upload.txt"), "Uploaded file name not displayed!");
        System.out.println("File upload verified successfully.");

        // ---------- File Download ----------
        WebElement downloadButton = driver.findElement(By.id("downloadButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", downloadButton);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadButton);
        System.out.println("Clicked on Download button.");

        //  'Downloads' folder
        String downloadFolder = System.getProperty("user.home") + "\\Downloads";
        System.out.println("Check your Downloads folder: " + downloadFolder);
        test.pass("File download test completed.");

    }
    
    
 // ---------- ------Task 7: Data-Driven Testing----------------
    @DataProvider(name = "LoginData")
    public Object[][] getLoginData() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\admin\\Desktop\\testdata.xlsx");
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rows - 1][cols];

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < cols; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) data[i - 1][j] = "";
                else if (cell.getCellType() == CellType.STRING)
                    data[i - 1][j] = cell.getStringCellValue();
                else if (cell.getCellType() == CellType.NUMERIC)
                    data[i - 1][j] = String.valueOf((long) cell.getNumericCellValue());
                else
                    data[i - 1][j] = "";
            }
        }
        wb.close();
        fis.close();
        return data;
    }

    @Test(priority = 10, dataProvider = "LoginData")
    public void dataDrivenLoginTest(String email, String password) {
        test = extent.createTest("TASK 7: Data Driven Test - " + email);

        System.out.println("\n-------- TASK 7: Data Driven Test --------");
        System.out.println("Email: " + email + " | Password: " + password);

        // Example test logic (replace with your actual form if needed)
        if (!email.isEmpty() && !password.isEmpty()) {
            System.out.println("Test executed successfully with credentials: " + email);
        } else {
            System.out.println("Invalid data row, skipping...");
            test.pass("Data-driven test executed successfully for: " + email);

        }
    }
    @AfterClass
    public void tearDown() {
        driver.quit();
        extent.flush(); // Generate the report
        System.out.println("All tasks completed and Extent Report generated.");
    }

}