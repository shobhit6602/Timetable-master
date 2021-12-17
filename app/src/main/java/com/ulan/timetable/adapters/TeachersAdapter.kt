package com.ulan.timetable.adapters

import com.ulan.timetable.receivers.setDoNotDisturbReceivers
import androidx.core.graphics.drawable.toBitmap
import com.ulan.timetable.utils.ShortcutUtils.Companion.createShortcuts
import com.mikepenz.aboutlibraries.LibsBuilder.withActivityTitle
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutIconShown
import com.mikepenz.aboutlibraries.LibsBuilder.withFields
import com.mikepenz.aboutlibraries.LibsBuilder.withLicenseShown
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutDescription
import com.mikepenz.aboutlibraries.LibsBuilder.withAboutAppName
import com.mikepenz.aboutlibraries.LibsBuilder.start
import saschpe.android.customtabs.CustomTabsHelper.Companion.addKeepAliveExtra
import saschpe.android.customtabs.CustomTabsHelper.Companion.openCustomTab
import android.database.sqlite.SQLiteOpenHelper
import com.ulan.timetable.profiles.ProfileManagement
import android.database.sqlite.SQLiteDatabase
import com.ulan.timetable.fragments.WeekdayFragment
import com.ulan.timetable.model.Week
import android.content.ContentValues
import com.ulan.timetable.model.Homework
import com.ulan.timetable.model.Teacher
import com.ulan.timetable.model.Exam
import androidx.appcompat.app.AppCompatActivity
import com.ulan.timetable.R
import android.annotation.SuppressLint
import com.ulan.timetable.adapters.WeekAdapter
import android.widget.AbsListView.MultiChoiceModeListener
import android.util.SparseBooleanArray
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.app.Activity
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.afollestad.materialdialogs.DialogAction
import android.content.Intent
import android.content.ComponentName
import android.content.pm.PackageManager
import android.app.PendingIntent
import android.app.AlarmManager
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import android.util.TypedValue
import com.ulan.timetable.activities.SettingsActivity
import com.github.stephenvinouze.shapetextdrawable.ShapeTextDrawable
import com.github.stephenvinouze.shapetextdrawable.ShapeForm
import android.graphics.Typeface
import androidx.core.graphics.drawable.IconCompat
import com.ulan.timetable.activities.MainActivity
import android.media.RingtoneManager
import com.ulan.timetable.receivers.NotificationDismissButtonReceiver
import android.app.NotificationChannel
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import android.graphics.drawable.ColorDrawable
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView
import android.widget.TextView.OnEditorActionListener
import android.view.inputmethod.EditorInfo
import android.view.View.OnFocusChangeListener
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import com.ulan.timetable.adapters.FragmentsTabAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.afollestad.materialdialogs.MaterialDialog.ListCallback
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import com.ulan.timetable.adapters.HomeworkAdapter
import android.provider.CalendarContract
import android.content.ActivityNotFoundException
import com.pd.chocobar.ChocoBar
import com.ulan.timetable.adapters.TeachersAdapter
import com.ulan.timetable.adapters.NotesAdapter
import com.ulan.timetable.adapters.ExamsAdapter
import androidx.cardview.widget.CardView
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import com.ulan.timetable.activities.TeachersActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ulan.timetable.appwidget.Dao.BaseDao
import kotlin.jvm.Synchronized
import com.ulan.timetable.appwidget.Dao.DBManager
import com.ulan.timetable.appwidget.Dao.DataBaseHelper
import com.ulan.timetable.appwidget.Dao.AppWidgetDao
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ulan.timetable.appwidget.DayAppWidgetService.DayAppWidgetRemoteViewsFactory
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import com.ulan.timetable.appwidget.DayAppWidgetService
import com.ulan.timetable.appwidget.DayAppWidgetProvider
import android.os.Bundle
import android.widget.SeekBar.OnSeekBarChangeListener
import com.ulan.timetable.appwidget.AppWidgetConstants
import androidx.preference.PreferenceFragmentCompat
import com.ulan.timetable.fragments.SettingsFragment
import com.ulan.timetable.activities.TimeSettingsActivity
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackMultiChoice
import android.text.InputType
import androidx.preference.SwitchPreferenceCompat
import com.ulan.timetable.receivers.DailyReceiver
import android.content.BroadcastReceiver
import android.graphics.Color
import android.net.Uri
import android.view.*
import android.widget.*
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.AdapterView.OnItemSelectedListener
import com.ulan.timetable.activities.ProfileActivity
import com.google.android.material.tabs.TabLayout
import androidx.core.view.GravityCompat
import com.mikepenz.aboutlibraries.LibsBuilder
import com.ulan.timetable.R.string
import com.ulan.timetable.activities.ExamsActivity
import com.ulan.timetable.activities.HomeworkActivity
import com.ulan.timetable.activities.NotesActivity
import com.ulan.timetable.activities.SummaryActivity
import info.isuru.sheriff.enums.SheriffPermission
import com.ajts.androidmads.library.SQLiteToExcel
import com.ajts.androidmads.library.SQLiteToExcel.ExportListener
import com.ajts.androidmads.library.ExcelToSQLite
import com.ajts.androidmads.library.ExcelToSQLite.ImportListener
import androidx.browser.customtabs.CustomTabsIntent
import saschpe.android.customtabs.WebViewFallback
import info.isuru.sheriff.helper.Sheriff
import info.isuru.sheriff.interfaces.PermissionListener
import com.ulan.timetable.activities.MainActivity.MyPermissionListener
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.widget.PopupMenu
import com.ulan.timetable.activities.NoteInfoActivity
import com.ulan.timetable.fragments.ProfileActivityFragment
import me.yaoandy107.ntut_timetable.CourseTableLayout
import me.yaoandy107.ntut_timetable.model.StudentCourse
import me.yaoandy107.ntut_timetable.model.CourseInfo
import com.ulan.timetable.activities.SummaryActivity.CustomCourseInfo
import com.github.tlaabs.timetableview.Schedule
import com.ulan.timetable.activities.SummaryActivity.CustomSchedule
import com.github.tlaabs.timetableview.TimetableView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ulan.timetable.fragments.TimeSettingsFragment
import com.ulan.timetable.utils.*
import java.lang.Exception
import java.util.*

/**
 * Created by Ulan on 08.10.2018.
 */
class TeachersAdapter(
    private val dbHelper: DbHelper,
    private val mActivity: AppCompatActivity,
    listView: ListView?,
    resource: Int,
    objects: ArrayList<Teacher?>
) : ArrayAdapter<Teacher?>(
    mActivity, resource, objects
) {
    private val mResource: Int
    val teacherList: ArrayList<Teacher>
    var teacher: Teacher? = null
        private set
    private val mListView: ListView

    private class ViewHolder {
        var name: TextView? = null
        var post: TextView? = null
        var phonenumber: TextView? = null
        var email: TextView? = null
        var cardView: CardView? = null
        var popup: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val name = Objects.requireNonNull(getItem(position)).getName()
        val post = Objects.requireNonNull(getItem(position)).getPost()
        val phonenumber = Objects.requireNonNull(getItem(position)).getPhonenumber()
        val email = Objects.requireNonNull(getItem(position)).getEmail()
        val color = Objects.requireNonNull(getItem(position)).getColor()
        teacher = Teacher(name, post, phonenumber, email, color)
        val holder: ViewHolder
        if (convertView == null) {
            val inflater = LayoutInflater.from(mActivity)
            convertView = inflater.inflate(mResource, parent, false)
            holder = ViewHolder()
            holder.name = convertView.findViewById(R.id.nameteacher)
            holder.post = convertView.findViewById(R.id.postteacher)
            holder.phonenumber = convertView.findViewById(R.id.numberteacher)
            holder.email = convertView.findViewById(R.id.emailteacher)
            holder.cardView = convertView.findViewById(R.id.teacher_cardview)
            holder.popup = convertView.findViewById(R.id.popupbtn)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        //Setup colors based on Background
        val textColor =
            ColorPalette.pickTextColorBasedOnBgColorSimple(color, Color.WHITE, Color.BLACK)
        holder.name!!.setTextColor(textColor)
        holder.post!!.setTextColor(textColor)
        holder.phonenumber!!.setTextColor(textColor)
        holder.email!!.setTextColor(textColor)
        ImageViewCompat.setImageTintList(
            convertView!!.findViewById(R.id.personimage),
            ColorStateList.valueOf(textColor)
        )
        ImageViewCompat.setImageTintList(
            convertView.findViewById(R.id.imageView4),
            ColorStateList.valueOf(textColor)
        )
        ImageViewCompat.setImageTintList(
            convertView.findViewById(R.id.imageView5),
            ColorStateList.valueOf(textColor)
        )
        ImageViewCompat.setImageTintList(
            convertView.findViewById(R.id.popupbtn),
            ColorStateList.valueOf(textColor)
        )
        convertView.findViewById<View>(R.id.line).setBackgroundColor(textColor)
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        holder.name.setText(teacher.getName())
        holder.post.setText(teacher.getPost())
        if (teacher.getPost() != null && !teacher.getPost().trim { it <= ' ' }.isEmpty()) {
            holder.post!!.setBackgroundResource(outValue.resourceId)
            holder.post!!.setOnClickListener { v: View? ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("geo:0,0?q=" + teacher.getPost())
                if (intent.resolveActivity(mActivity.packageManager) != null) {
                    try {
                        mActivity.startActivity(intent)
                    } catch (e2: ActivityNotFoundException) {
                        ChocoBar.builder().setActivity(mActivity)
                            .setText(mActivity.getString(string.no_navigation_app))
                            .setDuration(ChocoBar.LENGTH_LONG).red().show()
                    }
                }
            }
        }
        holder.phonenumber.setText(teacher.getPhonenumber())
        if (phonenumber != null && !phonenumber.trim { it <= ' ' }.isEmpty()) {
            holder.phonenumber!!.setBackgroundResource(outValue.resourceId)
            holder.phonenumber!!.setOnClickListener { v: View? ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phonenumber")
                mActivity.startActivity(intent)
            }
        }
        holder.email.setText(teacher.getEmail())
        if (teacher.getEmail() != null && !teacher.getEmail().trim { it <= ' ' }.isEmpty()) {
            holder.email!!.setBackgroundResource(outValue.resourceId)
            holder.email!!.setOnClickListener { v: View? ->
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:" + teacher.getEmail())
                try {
                    mActivity.startActivity(emailIntent)
                } catch (e: Exception) {
                    try {
                        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(emailIntent)
                    } catch (e2: ActivityNotFoundException) {
                        ChocoBar.builder().setActivity(mActivity)
                            .setText(mActivity.getString(string.no_email_app))
                            .setDuration(ChocoBar.LENGTH_LONG).red().show()
                    }
                }
            }
        }
        holder.cardView.setCardBackgroundColor(teacher.getColor())
        holder.popup!!.setOnClickListener { v: View? ->
            val theme = ContextThemeWrapper(
                mActivity, if (PreferenceUtil.isDark(
                        context
                    )
                ) R.style.Widget_AppCompat_PopupMenu else R.style.Widget_AppCompat_Light_PopupMenu
            )
            val popup = PopupMenu(theme, holder.popup!!)
            popup.inflate(R.menu.popup_menu)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.delete_popup -> {
                            dbHelper.deleteTeacherById(Objects.requireNonNull(getItem(position)))
                            dbHelper.updateTeacher(Objects.requireNonNull(getItem(position)))
                            teacherList.removeAt(position)
                            notifyDataSetChanged()
                            return@setOnClickListener true
                        }
                        R.id.edit_popup -> {
                            val alertLayout =
                                mActivity.layoutInflater.inflate(R.layout.dialog_add_teacher, null)
                            AlertDialogsHelper.getEditTeacherDialog(
                                dbHelper,
                                mActivity,
                                alertLayout,
                                teacherList,
                                mListView,
                                position
                            )
                            notifyDataSetChanged()
                            return@setOnClickListener true
                        }
                        else -> return@setOnClickListener onMenuItemClick(item)
                    }
                }
            })
            popup.show()
        }
        hidePopUpMenu(holder)
        return convertView
    }

    private fun hidePopUpMenu(holder: ViewHolder) {
        val checkedItems = mListView.checkedItemPositions
        if (checkedItems.size() > 0) {
            for (i in 0 until checkedItems.size()) {
                val key = checkedItems.keyAt(i)
                if (checkedItems[key]) {
                    holder.popup!!.visibility = View.INVISIBLE
                }
            }
        } else {
            holder.popup!!.visibility = View.VISIBLE
        }
    }

    init {
        mListView = listView!!
        mResource = resource
        teacherList = objects
    }
}