package org.hamcrest.kotlin.internal

import org.hamcrest.kotlin.MatchResult

public fun match(comparisonResult: Boolean, describeMismatch: () -> String): MatchResult =
        if (comparisonResult) {
            MatchResult.Match
        } else {
            MatchResult.Mismatch(describeMismatch())
        }

