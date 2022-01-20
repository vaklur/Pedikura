package com.example.pedikura.customers.add_customer.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedikura.R


class PhotosAdapter(
        private val mOnPhotosClickListener: OnPhotosClickListener,
        private val photos: ArrayList<Photo> = ArrayList())
    : RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    inner class PhotosViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val photo: ImageView = itemView.findViewById(R.id.item_photo_IV)
        val photoId: TextView = itemView.findViewById(R.id.item_number)
        val photoDelete:Button  = itemView.findViewById(R.id.delete_photo_BTN)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.photo_item, parent, false)
        val holder = PhotosViewHolder(view)

        holder.photoDelete.setOnClickListener {
            val position = holder.layoutPosition
            val model = photos[position]
            mOnPhotosClickListener.onDelete(model)
        }
        return holder
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = photos[position]
        holder.photo.setImageURI(photo.imageUri)
        holder.photoId.text = photo.idPhoto.toString()

    }

    override fun getItemCount(): Int {
        return  photos.size
    }

    fun addPhoto(model: Photo){
        photos.add(model)
        notifyItemInserted(photos.size)

    }

    fun removePhoto(model: Photo) {
        val position = photos.indexOf(model)
        photos.remove(model)
        notifyItemRemoved(position)
    }

    fun getNextItemId(): Int {
        var id = 1
        if (photos.isNotEmpty()) {
            // .last is equivalent to .size() - 1
            // we want to add 1 to that id and return it
            id = photos.last().idPhoto + 1
        }
        return id
    }

    fun getPhotos ():List<Photo>{
        return photos
    }

}