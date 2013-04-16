/*
 * SalesItemTestCase.groovy
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


/**
 * The class {@code SalesItemTestCase} contains common functions for all
 * functional tests concerning sales items.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemTestCase extends GeneralFunctionalTestCase {

    //-- Non-public methods ---------------------

    /**
     * Adds a new row to the step 1 pricing table.
     *
     * @return  the current number of rows in the step 1 pricing table after
     *          adding the new row
     */
    protected int addNewStep1TableRow() {
        driver.findElement(By.className('add-pricing-item-btn')).click()
        return numStep1TableRows
    }

    /**
     * Checks the values of the fields of the row with the given index in the
     * stated table in the show view.
     *
     * @param table     the web element representing the table
     * @param rowIdx    the zero-based index of the row
     * @param values    the values to check; {@code null} values and optionally
     *                  missing trailing elements are not checked
     */
    protected void checkStaticRowValues(WebElement table, int rowIdx,
                                        String... values)
    {
        WebElement row = table.findElement(By.xpath(".//tr[${rowIdx + 1}]"))
        for (int i = 0; i < values.length; i++) {
            String value = values[i]
            if (value != null) {
                assert value == row.findElement(By.xpath("./td[${i + 1}]")).text
            }
        }
    }

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }

    /**
     * Gets the number of rows in the price table.
     *
     * @return  the number of rows
     */
    protected int getNumStep1TableRows() {
        return step1Table.findElements(By.xpath("./tbody/tr")).size()
    }

    /**
     * Gets the web element representing the step 1 pricing table.
     *
     * @return  the step 1 pricing table web element
     */
    protected WebElement getStep1Table() {
        return driver.findElement(By.id('step1-pricing-items'))
    }

    /**
     * Gets the web element representing a cell in the step 1 pricing table.
     *
     * @param rowIdx    the zero-based index of the row
     * @param cellName  the class name of the cell
     * @return          the web element representing the cell
     */
    protected WebElement getStep1TableCell(int rowIdx, String cellName) {
        def row = getStep1TableRow(rowIdx)
        return row.findElement(By.className(cellName))
    }

    /**
     * Gets the input field with the given name in the stated row in the step 1
     * pricing table.
     *
     * @param rowIdx    the zero-based index of the table row
     * @param name      the name of the field such as "quantity", "name" etc.
     */
    protected WebElement getStep1TableInput(int rowIdx, String name) {
        return getInput("pricing.items[${rowIdx}].${name}")
    }

    /**
     * Gets the web element representing a row in the step 1 pricing table.
     *
     * @param rowIdx    the zero-based index of the row
     * @return          the web element representing the row
     */
    protected WebElement getStep1TableRow(int rowIdx) {
        return step1Table.findElement(By.xpath("./tbody/tr[${rowIdx + 1}]"))
    }

    /**
     * Gets the total of a row in the step 1 pricing table.
     *
     * @param rowIdx    the zero-based index of the row
     * @return          the total value
     */
    protected String getStep1TableRowTotal(int rowIdx) {
        getStep1TableInput(rowIdx, 'name').click()
        return getStep1TableCell(rowIdx, 'total-price').
            findElement(By.tagName('output')).
            text
    }

    protected String getStep1UnitPrice() {
        return step1Table.findElement(By.id('step1-unit-price')).text
    }

    protected String getStep1Total() {
        return step1Table.findElement(By.id('step1-total-price')).text
    }

    /**
     * Gets the value of the output field with the given name.
     *
     * @param name  the given name
     * @return      the value of the field
     */
    protected String getValue(String name) {
        driver.findElement(By.id('step1-pricing-quantity')).click()
        return driver.findElement(By.id(name)).text
    }

    /**
     * Moves the row with the given index one step downwards.
     *
     * @param rowIdx    the zero-based index of the table row
     */
    protected void moveRowDown(int rowIdx) {
        getStep1TableRow(rowIdx).findElement(By.className('down-btn')).click()
    }

    /**
     * Moves the row with the given index one step upwards.
     *
     * @param rowIdx    the zero-based index of the table row
     */
    protected void moveRowUp(int rowIdx) {
        getStep1TableRow(rowIdx).findElement(By.className('up-btn')).click()
    }

    /**
     * Removes the row with the given index.
     *
     * @param rowIdx    the zero-based index of the table row
     * @return          the current number of rows in the step 1 pricing table
     *                  after removing the row
     */
    protected int removeRow(int rowIdx) {
        getStep1TableRow(rowIdx).findElement(By.className('remove-btn')).click()
        return numStep1TableRows
    }

    /**
     * Sets the value of the input field with the given name in the stated row
     * in the step 1 pricing table.
     *
     * @param rowIdx    the zero-based index of the table row
     * @param name      the name of the field such as "quantity", "name" etc.
     * @param value     the value to set
     */
    protected void setStep1TableInputValue(int rowIdx, String name,
                                           String value)
    {
        setInputValue "pricing.items[${rowIdx}].${name}", value
    }
}
