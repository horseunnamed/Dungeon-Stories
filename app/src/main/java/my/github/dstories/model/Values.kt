package my.github.dstories.model

@JvmInline
value class ImagePath(val value: String)

@JvmInline
value class Id(val value: String) {
    init {
        require(value.isNotEmpty())
    }
}
