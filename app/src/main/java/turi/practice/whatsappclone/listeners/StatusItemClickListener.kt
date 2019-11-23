package turi.practice.whatsappclone.listeners

import turi.practice.whatsappclone.util.StatusListElement

interface StatusItemClickListener {
    fun onItemClicked(statusElement: StatusListElement)
}