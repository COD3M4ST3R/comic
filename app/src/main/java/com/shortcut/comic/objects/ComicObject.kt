package com.shortcut.comic.objects

class ComicObject
{
    private var day = ""
    private var month = ""
    private var year = ""
    private var number = 0
    private var link = ""
    private var news = ""
    private var safe_title = ""
    private var transcript = ""
    private var alt = ""
    private var img = ""
    private var title = ""
    private var fav = "0"

    fun set_day(day: String)
    {
        this.day = day
    }

    fun set_month(month: String)
    {
        this.month = month
    }

    fun set_year(year: String)
    {
        this.year = year
    }

    fun set_number(number: Int)
    {
        this.number = number
    }

    fun set_link(link: String)
    {
        this.link = link
    }

    fun set_news(news: String)
    {
        this.news = news
    }

    fun set_safe_title(safe_title: String)
    {
        this.safe_title = safe_title
    }

    fun set_transcript(transcript: String)
    {
        this.transcript = transcript
    }

    fun set_alt(alt: String)
    {
        this.alt = alt
    }

    fun set_img(img: String)
    {
        this.img = img
    }

    fun set_title(title: String)
    {
        this.title = title
    }

    fun set_fav(fav: String)
    {
        this.fav = fav
    }

    fun get_day(): String
    {
        return day
    }

    fun get_month(): String
    {
        return month
    }

    fun get_year(): String
    {
        return year
    }

    fun get_number(): Int
    {
        return number
    }

    fun get_link(): String
    {
        return link
    }

    fun get_news(): String
    {
        return news
    }

    fun get_safe_title(): String
    {
        return safe_title
    }

    fun get_transcript(): String
    {
        return transcript
    }

    fun get_alt(): String
    {
        return alt
    }

    fun get_img(): String
    {
        return img
    }

    fun get_title(): String
    {
        return title
    }

    fun get_fav(): String
    {
        return fav
    }

}
