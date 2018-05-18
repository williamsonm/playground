package core

sealed class Maybe<out A> {
    fun isJust(): Boolean =
        when(this) {
            is Just -> true
            is Nothing -> false
        }

    fun <B> map(f: (A) -> B): Maybe<B> = when(this) {
        is Just -> Just(f(this.a))
        is Nothing -> Nothing()
    }

}
class Just<A>(val a: A): Maybe<A>()
class Nothing<A>: Maybe<A>()
