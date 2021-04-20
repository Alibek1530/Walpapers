package uz.ali.imagefon.models

data class SearchResults(
    var total: Int,
    var totalPages: Int,
    var results: List<Photo>
)