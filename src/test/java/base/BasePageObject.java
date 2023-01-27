package base;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ui.Browser.drv;

public class BasePageObject {

    private static final Logger logger = LoggerFactory.getLogger(BasePageObject.class.getSimpleName());

    protected BasePageObject() {
        logger.info("Invoking [BasePageObject] default constructor....");
        PageFactory.initElements(new AjaxElementLocatorFactory(drv, 4), this);
    }

}
