/*
 * ProductController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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


/**
 * The class {@code ProductController} contains actions which manage products.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class ProductController extends SalesItemController<Product> {

    //-- Constructors ---------------------------

    ProductController() {
        super(Product)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def get(Long id) {
        super.get id
    }

    def index() {
        super.index()
    }

    def save() {
        super.save()
    }

    def selectorList() {
        super.selectorList()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }
}
