package cn.thecover.media.feature.review_data.basic_widget.intent

/**
 *  Created by Wing at 15:35 on 2025/8/12
 *
 */

sealed class ReviewUIIntent {
    data class UpdateDepartmentDataFilter(val state: String, val time: String) :
        ReviewUIIntent()

    data class UpdateDepartmentTaskFilter(val time: String) :
        ReviewUIIntent()

    data class UpdateDepartmentTopFilter(val time: String) :
        ReviewUIIntent()

    data class UpdateManuscriptTopFilter(val state: String, val time: String) :
        ReviewUIIntent()

    data class UpdateManuscriptDiffusionFilter(
        val state: String? = null,
        val time: String? = null,
        val searchType: String? = null,
        val searchText: String? = null
    ) :
        ReviewUIIntent()

    data class UpdateManuscriptReviewFilter(
        val state: String? = null,
        val time: String? = null,
        val searchType: String? = null,
        val searchText: String? = null
    ) : ReviewUIIntent()


}