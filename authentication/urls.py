from django.conf.urls import *
from django.conf import settings
from django.conf.urls.static import static
from django.contrib import admin
from . import views


#import social_network
#from social_network import views as socialview
#import search
#from search_app import views as search_view
#from django.contrib import admin
from .views import *

#import edite_data
#from edite_data import views as viewed

urlpatterns = [

	#url(r'^$', index),
	
	url(r'^index/proctorlogin/',views.login_proctor),
	url(r'^index/facultylogin/',views.login_faculty),
	url(r'^index/studentmemberlogin/',views.login_student_member),
	
	url(r'^index/proctoreregistration/',views.add_proctor),
	url(r'^index/facultyregistration/',views.add_faculty),
	url(r'^index/studentmemberregistration/',views.add_student_member),

	url(r'^index/changesettings/',views.change_settings),
	url(r'^index/changepassword/',views.change_password),
	url(r'^index/forgotpassword/',views.forgot_password),
	url(r'^index/showdetails/',views.show_details),

	url(r'^index/showparticularauthority/',views.particular_authority),
	
	url(r'^index/$',views.index),
	url(r'^student/',views.index_student),
	url(r'^proctor/',views.index_proctor),
	url(r'^faculty/',views.index_faculty),
	url(r'^logout/',views.logout),
	url(r'^index/studentregistration/',views.student_registration),
	url(r'^index/studentlogin/',views.student_login),
	url(r'^index/changestudentsettings/',views.update_student_detail),
	url(r'^index/showstudentdetail/',views.show_student_detail),
	url(r'^index/changestudentpassword/',views.student_change_password),
	url(r'^index/addcomplain/',views.add_complain),
	url(r'^index/seecomplains/',views.see_complains),
	url(r'^index/seeparticularcomplain/',views.see_particular_complain),
	url(r'^index/deletecomplain/',views.delete_complain),
	url(r'^index/sharecomplain/',views.share_complain),
	url(r'^index/seestudentnotification/',views.see_student_notification),
	url(r'^index/seeauthoritynotification/',views.see_authority_notification),
	url(r'^index/deletestudentnotification/',views.delete_student_notification),
	url(r'^index/deleteauthoritynotification/',views.delete_authority_notification),
	url(r'^index/addvotingcomplain/',views.voting_complain),
	#url(r'^index/faltu/',views.faltu),

	url(r'^index/updatecomplainstatus/',views.update_complain_status),

	url(r'^index/forgotstudentpassword/',views.student_forgot_password),








	

	]

if settings.DEBUG:
	urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)