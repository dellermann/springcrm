/*
 * InvoicingTransactionTestCase.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code InvoicingTransactionTestCase} represents a generic base
 * class for test cases concerning the invoicing transactions such as quotes,
 * invoices, dunnings etc.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class InvoicingTransactionTestCase extends GeneralFunctionalTestCase {

    //-- Constants ------------------------------

    protected static final String [] CELL_NAMES = [
            'number', 'quantity', 'unit', 'name', 'description', 'unitPrice',
            'total', 'tax'
        ] as String[]


    //-- Non-public methods ---------------------

    /**
     * Adds a new row to the price table.
     *
     * @return  the current number of rows in the price table after adding the
     *          new row
     */
    protected int addNewPriceTableRow() {
        driver.findElement(By.className('add-invoicing-item-btn')).click()
        return numPriceTableRows
    }

    /**
     * Checks the values of the input fields and the total value of the row
     * with the given index.
     *
     * @param rowIdx    the zero-based index of the row
     * @param values    the values to check in the order number, quantity,
     *                  unit, name, description, unitPrice, total, tax;
     *                  {@code null} values and optionally missing trailing
     *                  elements are not checked
     * @see             #CELL_NAMES
     */
    protected void checkRowValues(int rowIdx, String... values) {
        String prefix = "items[${rowIdx}].".toString()
        for (int i = 0; i < values.length; i++) {
            String value = values[i]
            if (value != null) {
                String cellName = CELL_NAMES[i]
                if (cellName == 'total') {
                    assert value == getPriceTableRowTotal(rowIdx)
                } else {
                    assert value == getInputValue(prefix + cellName)
                }
            }
        }
    }

    /**
     * Checks the values of the fields of the row with the given index in the
     * show view.
     *
     * @param rowIdx    the zero-based index of the row
     * @param values    the values to check in the order number, quantity,
     *                  unit, name and description, unitPrice, total, tax;
     *                  {@code null} values and optionally missing trailing
     *                  elements are not checked
     */
    protected void checkStaticRowValues(int rowIdx, String... values) {
        WebElement row = getPriceTableRow(rowIdx)
        assert "${rowIdx + 1}." == row.findElement(By.xpath('./td[1]')).text
        for (int i = 0; i < values.length; i++) {
            String value = values[i]
            if (value != null) {
                assert value == row.findElement(By.xpath("./td[${i + 2}]")).text
            }
        }
    }

    /**
     * Checks the tax rates in the price table.  The method checks the number
     * of tax rates, their order and values.
     *
     * @param taxRates  a list of tuple lists representing the tax rates to
     *                  check; each tuple contains the tax rate as first
     *                  element and the tax value as second element
     */
    protected void checkTaxRates(List<List<String>> taxRates) {
        List<WebElement> taxRateRows = driver.findElements(By.className('tax-rate-sum'))
        int n = taxRates.size()
        assert n == taxRateRows.size()

        By labelBy = By.xpath('./td[@class="label"]/label')
        By priceBy = By.className('total-price')
        for (int i = 0; i < n; i++) {
            List<String> item = taxRates[i]
            WebElement row = taxRateRows[i]
            assert "${item[0]} % MwSt." == row.findElement(labelBy).text
            assert "${item[1]} €" == row.findElement(priceBy).text
        }
    }

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml', 'test-data/sales-items.xml']
    }

    protected String getDiscountPercentAmount() {
        return driver.findElement(By.id('discount-from-percent')).text
    }

    /**
     * Gets the number of rows in the price table.
     *
     * @return  the number of rows
     */
    protected int getNumPriceTableRows() {
        return priceTable.findElements(By.xpath("./tbody[1]/tr")).size()
    }

    /**
     * Gets the web element representing the price table.
     *
     * @return  the price table web element
     */
    protected WebElement getPriceTable() {
        return driver.findElement(By.className('price-table'))
    }

    /**
     * Gets the web element representing a cell in the price table.
     *
     * @param rowIdx    the zero-based index of the row
     * @param cellName  the class name of the cell
     * @return          the web element representing the cell
     */
    protected WebElement getPriceTableCell(int rowIdx, String cellName) {
        def row = getPriceTableRow(rowIdx)
        return row.findElement(By.className(cellName))
    }

    /**
     * Gets the web element representing a row in the price table.
     *
     * @param rowIdx    the zero-based index of the row
     * @return          the web element representing the row
     */
    protected WebElement getPriceTableRow(int rowIdx) {
        return priceTable.findElement(By.xpath("./tbody[1]/tr[${rowIdx + 1}]"))
    }

    /**
     * Gets the total of a row in the price table.
     *
     * @param rowIdx    the zero-based index of the row
     * @return          the total value
     */
    protected String getPriceTableRowTotal(int rowIdx) {
        return getPriceTableCell(rowIdx, 'total-price').
            findElement(By.tagName('output')).
            text
    }

    protected String getSubtotalGross() {
        return driver.findElement(By.id('subtotal-gross')).text
    }

    protected String getSubtotalNet() {
        return driver.findElement(By.id('subtotal-net')).text
    }

    protected String getTotal() {
        return driver.findElement(By.id('total-price')).text
    }

    /**
     * Moves the row with the given index one step downwards.
     *
     * @param rowIdx    the zero-based index of the table row
     */
    protected void moveRowDown(int rowIdx) {
        getPriceTableRow(rowIdx).findElement(By.className('down-btn')).click()
    }

    /**
     * Moves the row with the given index one step upwards.
     *
     * @param rowIdx    the zero-based index of the table row
     */
    protected void moveRowUp(int rowIdx) {
        getPriceTableRow(rowIdx).findElement(By.className('up-btn')).click()
    }

    /**
     * Opens a sales item selector dialog by clicking the corresponding link in
     * the table row with the given index.  After opening the dialog the stated
     * link is clicked.
     *
     * @param rowIdx        the zero-based index of the table row
     * @param type          the type of selector which is to open; may be
     *                      either {@code products} or {@code services}
     * @param selectLink    the label of the link which is to click in the
     *                      selector dialog
     */
    protected void openSelectorAndSelect(int rowIdx, String type,
                                         String selectLink)
    {
        getPriceTableRow(rowIdx).
            findElement(By.className("select-btn-${type}")).
            click()
        def by = By.id("inventory-selector-${type}")
        def wait = new WebDriverWait(driver, 5)
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(by))
        dialog.findElement(By.linkText(selectLink)).click()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by))
    }

    /**
     * Removes the row with the given index.
     *
     * @param rowIdx    the zero-based index of the table row
     * @return          the current number of rows in the price table after
     *                  removing the row
     */
    protected int removeRow(int rowIdx) {
        getPriceTableRow(rowIdx).findElement(By.className('remove-btn')).click()
        return numPriceTableRows
    }
}
