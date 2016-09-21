package org.amcworld.springcrm

class SearchController {

    SearchService searchService

    def index(String query) {
        [searchResults: searchService.search(query)]
    }
}
