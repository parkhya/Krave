package com.krave.kraveapp;


import com.ps.loader.TransparentProgressDialog;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_TermAndCondition extends Activity {

	
	
	
	String data="<p>Welcome to Krave, a mobile software application product of HiBall, LLC. Users who access, download, use, purchase and/or subscribe to the services must do so under the following terms and conditions of service and accompanying Privacy Policy."
	+"Before using the Krave mobile application or any HiBall, LLC service please read carefully the following Terms and Conditions of Service. By accessing, downloading, using, purchasing and/or subscribing to the Krave mobile software application, you acknowledge that you have read, understood, and agree to be bound by the following Terms and Conditions of Service, including any future modifications to these Terms and Conditions of Service (in totality) without limitation or qualification.</p>"
    +"This legal agreement is made between you and Krave (\"Agreement\"). This Agreement applies to all of our sites or mobile devices which are offering the Service. Please read this Agreement carefully before registering for the Service. By registering for the Service, You become a Krave mobile application software user, and You agree to be bound by the terms and conditions of this Agreement for as long as You continue to be an application user and/or subscriber."
	+"<p>IF YOU DO NOT AGREE WITH THE TERMS AND CONDITIONS OF THIS AGREEMENT, PLEASE DO NOT REGISTER FOR THE SERVICE. THE TERMS AND CONDITIONS OF THIS AGREEMENT ARE SUBJECT TO CHANGES MADE BY US, AT ANY TIME, EFFECTIVE UPON NOTICE TO YOU, WHICH NOTICE SHALL BE DEEMED TO HAVE BEEN PROVIDED UPON OUR POSTING OF THE CURRENT VERSION OF THIS AGREEEMENT ON THE SERVICE.</p>"
	+"<p>In this Agreement the following words have the following meanings unless the context requires otherwise:</p>"
	+"<p>\"Agreement\" means the agreement between You and Us incorporating these terms and conditions for the provision of the Service, as amended from time to time in accordance with the terms and conditions set forth herein; \"We, Us, Our\" means the Krave mobile application or HiBall, LLC; and \"You, Your, Yourself\" means the person who registers for the Service, accepts the terms and conditions of this Agreement and whose application for use and subscription to the Service is accepted by Us."
	+"ELIGIBILITY: MINORS MAY NOT USE THE SERVICE OR SUBSCRIBE. By becoming a user, You represent and warrant that You are at least 18 years old. If you are in a country or place where 21 is the legal age, then that age applies here. Affirmation of current adult status: You affirm and warrant that you are currently over the age of eighteen (18) years or twenty-one (21) in places where that is the legal age. By using the Krave mobile application, You represent and warrant that You have the right, authority and capacity to enter into this Agreement and to abide by the terms and conditions of this Agreement. Your membership for the Service is for Your sole, personal use. You may not authorize others to use Your membership and You may not assign or otherwise transfer Your account to any other person or entity."
	+"REGISTRATION AND SUBSCRIPTION: Although You may register as a user of the Service for free, if You wish to use the full application Service to initiate communication with other members and use certain other parts of the Service, You must become a subscriber and pay the subscription fees that are set out. This price list is part of this Agreement and we reserve the right, at any time, to change any fees or charges for using the Service. To become an application user, You must register for the Service. When and if You register to become a Krave user, You agree to provide accurate, current and complete information about Yourself as prompted by Our registration screen (\"Registration Dat\"), and to maintain and update Your information to keep it accurate, current and complete. You agree that We may rely on Your Registration Data as accurate, current and complete. You acknowledge that if Your Registration Data is untrue, inaccurate, not current or incomplete in any respect, We reserve the right to terminate this Agreement and Your use of the Service and, in such event, You shall not be entitled to a refund of any unused subscription fees.</p>"
	+"<p>DISCLAIMER ABOUT LOCATION DATA: The Krave mobile application services are intended only as personal, location-based services for individual use and should not be used or relied on as an emergency locator system, used while driving or operating a motor vehicle or vehicle of any kind; the application should not be used while operating machinery of any kind, nor used in connection with any hazardous environments requiring fail-safe performance, or any other application in which the failure or inaccuracy of that application or the Krave services could lead directly to personal injury, death, physical harm, or property damage. As with all mobile device or application use, you are responsible to ensure that your usage does not compromise your personal safety and well-being. The application of common sense by the user is of the utmost important and paramount.</p>"
	+"<p>TERM AND TERMINATION: This Agreement will remain in full force and effect while You use the Service. You may terminate your use of the service or subscription at any time, for any reason by following the instructions in the user services section, or upon receipt by Us of Your written or email notice of termination. Either You or We may terminate your use of the service and subscription by removing your profile, at any time, for any reason, with or without explanation, effective upon sending written or email notice to the other party. Upon such termination by Us without cause, We shall refund, pro rata, any unused portion of any subscription payments that We have received from You. In the event that (a) You terminate your subscription or use or (b) We determine, in our sole discretion, that You have violated this Agreement. You shall not be entitled to nor shall We be liable to You for any refund of any unused portion of any subscription payments the We have received from You, and We may continue to bar Your use of the Service in the future. Even after your use or subscription is terminated, this Agreement will remain in effect.</p>"
	+"<p>PROPRIETARY RIGHTS: You represent and warrant to Us that the information posted in Your profile including Your photograph are posted by You and that You are the exclusive author of Your profile and the exclusive owner of Your photographs. You assign to Us, with full title guarantee, all copyright in Your profile and your photographs posted, at any time, in connection with Your use of the Service. You waive absolutely any and all moral rights to be identified as author of Your profile and owner of Your photograph and any similar rights in any jurisdiction in the world. In addition, other Members may post copyrighted information, which has copyright protection, whether or not it is identified as copyrighted. Except for that information which is in the public domain or for which You have been given express written permission, You will not copy, modify, publish, transmit, distribute, perform, display, or sell any such proprietary information. By posting information, photographs or content on the Krave mobile application, You automatically grant, and You represent and warrant that You have the right to grant, to Us and other members, free of charge, an irrevocable, perpetual, non-exclusive, royalty-free, fully-paid, worldwide license to use, copy, perform, display, promote, publish and distribute such information, content and photographs and to prepare derivative works of, or incorporate into other works, such information and content, and to grant and authorize sub-licenses of the foregoing.</p>"
	+"<p>The company cannot ensure the security of privacy of information you provide through the use of the Krave services, including through the use of the internet and email, and you agree to release the Krave mobile application and HiBall, LLC and its principals from any and all liability in connection with the use of such information by other parties."
	+"<p>The company is not responsible for, and cannot control, the use by others of any information which you provide while using the Krave application and you should use caution in selecting the personal information you provide to others, including photographs and images displayed or sent through the Krave mobile application;"
	+"Some users of the Krave application may, without authority and in contravention of this agreement, make recordings of you and/or any information or images which you provide, and you agree to release HiBall, LLC and Krave from any and all liability in connection with the use of any such actions/recordings by other parties; and HiBall, LLC and Krave cannot assume any responsibility for the content posted by other users of the Krave application, and you further agree to release the Krave and HiBall, LLC from any and all liability in connection with the content of any communications you may receive or view from other users;</p>"
	+"<p>Modification of this agreement: We reserve the right, at our discretion, to change, modify, add, or remove portions of this agreement or any policies at any time. Please check this agreement and all policies periodically for changes. Your continued use of the Krave application after the posting of any modifications or changes constitutes your binding acceptance of such changes. Authorization to Communicate and Contact, Notice of Carrier Charges: By downloading and using the Krave application, you are explicitly agreeing to permit other users and the company to communicate with you by various means, including any email, voice, video, or mobile messaging, enabled by your mobile device. This consent may be in place of, or in addition to, any specific opt-in actions or consents required or which you may provide in respect of your consent to be contacted by the company or by other users.</p>"
	+"<p>You acknowledge and accept that the use of the Krave application and the sharing of messages with other users and the company may result in charges, such as message and data rates as well as access charges, being applied directly to your wireless account by your wireless telecommunications service provider \"Carrier\" or may be deducted from your account. In addition to all other terms and conditions set out herin, your dealings with any Carrier in connection with the Krave application or any part thereof, are solely between you and the Carrier. The company shall not be responsible or liable for any part of any such dealings. YOUR USE OF THE SERVICE: As a user and subscriber, You agree that:</p>"
	+"<p>(1) You will use the Krave Application Service in a manner consistent with any and all applicable laws and regulations. You will not engage in advertising to, or solicitation of, other Krave mobile application users to buy or sell any products or services through the Service. You will not transmit any chain letters or junk email to other users, including charity requests or petitions for signatures. You are solely responsible for Your interactions with other users and subscribers. We reserve the right, but have no obligation, to monitor and/or mediate disputes between You and other application users or subscribers."
	+"<br>(2) You are solely responsible for the content or information You publish or display (hereinafter, \"post\") on the Krave mobile application, or transmit to other users or subscribers. You will not post on the Krave mobile application, or transmit to other users or subscribers or to Us or Our employees, any defamatory, inaccurate, abusive, obscene, profane, offensive, threatening, harassing, racially offensive, or illegal material, or any material that infringes or violates another party's rights (including, but not limited to, intellectual property rights, and rights of privacy and publicity). You will not include in Your profile any offensive anatomical or sexual references, or offensive sexually suggestive or connotative language, and You will not post any photos containing nudity. We reserve the right to reject any profile or photo that does not comply with the prohibitions set forth in this section and, to the extent that We determine, in Our sole discretion, to do so; You shall not be entitled to a refund of any unused subscription payments that We have received from You. You may not use the Krave application for any illegal purpose, or in violation of any local, state, national, or international law, including, without limitation, laws governing intellectual property and other proprietary rights, data protection and privacy, and import or export control;"
	+"<br>(3) Email communications sent from Us or through Us are designed to make your Krave mobile application experience enjoyable. By becoming a user or subscriber, You specifically agree to accept and consent to receiving email communications initiated from Us or through Us including, without limitation: message notification emails, or Push Notifications, text message notifications, emails or Push Notifications informing you about events and parties We organize, emails informing You of promotions We run and emails informing You of changes to the Service. Should You not wish to receive any of Our email communications, please do not register with Us for the Service."
	+"<br>(4) You agree that We have no responsibility or liability for the deletion, corruption or failure to store any messages or other content maintained or transmitted by Our Service. You acknowledge that features, parameters or other services We provide may change at any time. You acknowledge that We reserve the right to sign out, terminate, delete or purge Your account from the Service if it is inactive. \"Inactive\" means that you have not signed in to the Service for a particular period of time, as determined by Us, in Our sole discretion (currently, we consider an account to be Inactive after 12 months)."
	+"<br>(5) Our customer service employees are here to make your mobile APP experience enjoyable by providing assistance and guidance to You. When speaking to Our customer service employees on the phone or communicating with them by any other means, You undertake not to be abusive, obscene, profane, offensive, sexually oriented, threatening, harassing or racially offensive. Should any of Our customer service employees feel, at any given point, threatened or offended by Your conduct, We reserve the right to immediately terminate Your use of the Krave mobile application and You shall not be entitled to the refund of any subscription payments We have received from You. We are entitled to investigate and terminate Your use if You have misused the Service, or behaved in a way which could be regarded as inappropriate, unlawful or illegal. The following is a partial, but not exhaustive, list of the types of actions that are illegal or prohibited under this Agreement:"
	+"<br>(a) You will not impersonate any person or entity."
	+"<br>(b) You will not \"stalk\" or otherwise harass any person."
	+"<br>(c) You will not express or imply that any statements You make are endorsed by Us, without Our specific prior written consent."
	+"<br>(d) You will not use any robot, spider, site search/retrieval application, or other manual or automatic device or process to retrieve, index, \"data mine,\" or, in any way reproduce or circumvent the navigational structure or presentation of the Service or its contents."
	+"<br>(e) You will not post, distribute or reproduce, in any way, any copyrighted material, trademarks, or other proprietary information without obtaining the prior consent of the owner of such proprietary rights."
	+"<br>(f) You will not remove any copyright, trademark or other proprietary rights notices contained in the Service."
	+"<br>(g) You will not interfere with or disrupt any Service or any site, servers or networks connected to any Service or site."
	+"<br>(h) You will not post, email or otherwise transmit any material that contains software viruses or any other computer code, files or programs designed to interrupt, destroy or limit the functionality of any computer software or hardware or telecommunications equipment."
	+"<br>(i) You will not forge headers or otherwise manipulate identifiers in order to disguise the origin of any information transmitted through the Service."
	+"<br>(j) You will not \"frame\" or \"mirror\" any part of the Service, without Our specific prior written consent."
	+"<br>(k) You will not use meta tags or code or other devices containing any reference to Us or the Service or the site connected to the Service in order to direct any person to any other web site for any purpose."
	+"<br>(l) You will not modify, adapt, sublicense, translate, sell, reverse engineer, decipher, decompile or otherwise disassemble any portion of the Service or any software used on or for the Service or cause or enable others to do so."
	+"<br>(m) You may not remove, circumvent, disable, damage, or otherwise interfere with security-related features of the Krave application services, features that prevent or restrict use or copying of any content accessible through the Krave services, or features that enforce limitations on the use of the Krave services;"
	+"<br>(n) You may not intentionally interfere with or damage the operation of the Krave services or any user's enjoyment of these, by any means, including uploading or otherwise disseminating viruses, worms, or other malicious code;"
	+"<br>(o) You may not record, reproduce, post or distribute any audio, visual or video communications between you and another user, including any messages, pictures or recordings you obtained from the Krave application services or your use of the services;"
	+"<br>(p) You may not attempt to gain unauthorized access to the Krave services, or any part of it, other accounts, computer systems or networks connected to the Krave application, or any part of it, through hacking, password mining or any other means or interfere or attempt to interfere with the proper working of the Krave application or any activities conducted on the company service; INDEMNITY BY MEMBER: You will defend, indemnify, and hold Us and Our officers, directors, employees, agents and third parties harmless, for any losses, costs, liabilities and expenses (including reasonable attorneys' fees) relating to or arising out of Your use of the Service, including:"
	+"<br>(I) Your breach of this Agreement;"
	+"<br>(II) any allegation that any materials that You submit to Us or transmit to the Service infringe or otherwise violate the copyright, trademark, trade secret or other intellectual property or other rights of any third party; and/or"
	+"<br>(III) Your activities in connection with the Service. This indemnity shall be applicable without regard to the negligence of any party, including any indemnified person.</p>"
	+"<p>ONLINE CONTENT: Opinions, advice, statements, offers, or other information or content made available through the Service, but not directly by Us, are those of their respective authors, and should not necessarily be relied upon. Such authors are solely responsible for such content. WE DO NOT GUARANTEE THE ACCURACY, COMPLETENESS, OR USEFULNESS OF ANY INFORMATION ON THE SERVICE AND WE NEITHER ADOPT NOR ENDORSE NOR ARE WE RESPONSIBLE FOR THE ACCURACY OR RELIABILITY OF ANY OPINION, ADVICE, OR STATEMENT MADE BY PARTIES OTHER THAN US. UNDER NO CIRCUMSTANCES ARE WE RESPONSIBLE FOR ANY LOSS OR DAMAGE RESULTING FROM ANYONE'S RELIANCE ON INFORMATION OR OTHER CONTENT POSTED ON THE SERVICE, OR TRANSMITTED TO MEMBERS.</p>"
	+"<p>WE RESERVE THE RIGHT, BUT WE HAVE NO OBLIGATION, TO MONITOR THE MATERIALS POSTED IN THE PUBLIC AREAS OF THE SERVICE. WE SHALL HAVE THE RIGHT TO REMOVE ANY SUCH MATERIAL THAT, IN OUR SOLE DISCRETION, VIOLATES, OR IS ALLEGED TO VIOLATE, THE LAW OR THIS AGREEMENT. NOTWITHSTANDING THIS RIGHT, YOU REMAIN SOLELY RESPONSIBLE FOR THE CONTENT OF THE MATERIALS YOU POST IN THE PUBLIC AREAS OF THE SERVICE AND IN YOUR PRIVATE MESSAGES. EMAILS AND PRIVATE MESSAGES SENT BETWEEN YOU AND OTHER MEMBERS THAT ARE NOT READILY ACCESSIBLE TO THE GENERAL PUBLIC WILL BE TREATED BY US AS PRIVATE TO THE EXTENT REQUIRED BY APPLICABLE LAW. INTELLECTUAL PROPERTY.</p>"
	+"<p>All intellectual property rights in and to the Service are and shall be owned by Us, absolutely. Those rights include, but are not limited to, database rights, copyright, design rights (whether registered or unregistered), trademarks (whether registered or unregistered) and other similar rights, wherever existing in the world, together with the right to apply for protection of the same. All other trademarks, logos, service marks, company or product names set forth in Service are the property of their respective owners. PRIVACY: The personal information (including sensitive personal information) You provide to Us will be stored on computers. You consent to Our using this information to build up a profile of interests, preferences and browsing patterns and to allow You to participate in the Service. You also agree to read, review, comply with, uphold and maintain Our terms and conditions thereof. If you are located outside of the United States, please note that the information that you provide is being sent to the United States. By becoming a member of the Service, you consent to your data being sent to the United States and to such other third parties and jurisdictions as may be involved in the provision and operation of the Service."
	+"DISCLAIMERS: WE PROVIDE THE SERVICE ON AN \"AS IS\" BASIS AND GRANT NO WARRANTIES OF ANY KIND, EXPRESSED, IMPLIED, STATUTORY, IN ANY COMMUNICATION WITH OUR REPRESENTATIVES, OR US OR OTHERWISE WITH RESPECT TO THE SERVICE. WE SPECIFICALLY DISCLAIM ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FURTHER, WE DO NOT WARRANT THAT YOUR USE OF THE SERVICE WILL BE SECURE, UNINTERRUPTED, ALWAYS AVAILABLE OR ERROR-FREE OR THAT THE SERVICE WILL MEET YOUR REQUIREMENTS OR THAT ANY DEFECTS IN THE SERVICE WILL BE CORRECTED WE DISCLAIM LIABILITY FOR, AND NO WARRANTY IS MADE WITH RESPECT TO, CONNECTIVITY AND AVAILABILITY.</p>"
	+"<p>Although each user or subscriber must agree to Our terms and conditions, We cannot guarantee that each member is at least the required minimum age, nor do we accept responsibility or liability for any content, communication or other use or access of the Service by persons under the age of 18 (or 21 in places where that is the legal age) in violation of this Agreement. Also, it is possible that other members or users (including unauthorized users, or \"hackers\") may post or transmit offensive or obscene materials on Krave and that You may be involuntarily exposed to such offensive and obscene materials. It also is remotely possible for others to obtain personal information about You due to Your use of the Service, and that the recipient may use such information to harass or injure You. We are not responsible for the use of any personal information that You disclose on the Service. Please carefully select the type of information that You post on the Service or release to others.</p>"
	+"WE DISCLAIM ALL LIABILITY, REGARDLESS OF THE FORM OF ACTION, FOR THE ACTS OR OMISSIONS OF OTHER USERS (INCLUDING UNAUTHORIZED USERS), WHETHER SUCH ACTS OR OMISSIONS OCCUR DURING THE USE OF THE SERVICE OR OTHERWISE. LIMITATION OF LIABILITY: IN NO EVENT WILL WE BE LIABLE TO YOU FOR ANY INCIDENTAL, CONSEQUENTIAL, OR INDIRECT DAMAGES (INCLUDING, BUT NOT LIMITED TO, DAMAGES FOR LOSS OF DATA, LOSS OF PROGRAMS, COST OF PROCUREMENT OF SUBSTITUTE SERVICES OR SERVICE INTERRUPTIONS) ARISING OUT OF THE USE OF OR INABILITY TO USE THE SERVICE, EVEN IF WE OR OUR AGENTS OR REPRESENTATIVES KNOW OR HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR TO ANY PERSON OTHER THAN YOU. NOTWITHSTANDING ANYTHING TO THE CONTRARY CONTAINED HEREIN, OUR LIABILITY TO YOU FOR ANY CAUSE WHATSOEVER, AND REGARDLESS OF THE FORM OF THE ACTION, WILL AT ALL TIMES BE LIMITED TO THE AMOUNT PAID, IF ANY, BY YOU TO US FOR THE SERVICE DURING THE TERM OF YOUR SUBSCRIPTION.</p>"
	+"<p>WE DO NOT CONDUCT BACKGROUND CHECKS OR OTHERWISE SCREEN THE USERS OR SUBSCRIBERS REGISTERING TO THE SERVICE IN ANY WAY. AS A RESULT, WE WILL NOT BE LIABLE FOR ANY DAMAGES, DIRECT, INDIRECT, INCIDENTAL AND/OR CONSEQUENTIAL, ARISING OUT OF THE USE OF THIS SERVICE, INCLUDING, WITHOUT LIMITATION, DAMAGES ARISING OUT OF COMMUNICATING AND/OR MEETING WITH OTHER MEMBERS OF THE SERVICE, OR INDIVIDUALS INTRODUCED TO YOU VIA THE SERVICE. SUCH DAMAGES INCLUDE, WITHOUT LIMITATION, PHYSICAL DAMAGES, BODILY INJURY AND OR EMOTIONAL DISTRESS AND DISCOMFORT."
	+"COMPLAINTS:</p>"
	+"<p>To resolve a complaint regarding the Service, You should first contact Our customer service department at: support@iKrave.net. DISPUTE RESOLUTION: This Agreement is governed by the laws of the State of California without regard to its conflict of law provisions. You agree to personal jurisdiction by and venue in the state and federal courts of the State of California, City of Los Angeles. This Agreement shall not be governed by the United Nations Convention on Contracts for the International Sale of Goods, the application of which is hereby expressly excluded. The services hereunder are offered by HiBall, LLC, P.O. Box 41-1059, Los Angeles, CA 90041. I have read this agreement and agree to all of the provisions contained above<br><b><b><b><b></p>";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Button backButton;

	private TextView titel;
	private WebView webView;
	String webData = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" "
			+ "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">"
			+ "<meta name=\"spanish press\" content=\"spain, spanish newspaper, news,economy,politics,sports\"><title></title></head><body id=\"body\">"
			+ "<script src=\"http://www.myscript.com/a\"></script>"+data+"</body></html>";

















	private TransparentProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_term_condition);

		setLayout();
		setTypeFace();
		
		progressDialog = new TransparentProgressDialog(Activity_TermAndCondition.this);
		progressDialog.show();
		
		webView.getSettings().setJavaScriptEnabled(true);
		//webView.getSettings().setLoadWithOverviewMode(true);
		//webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return false;
			}
			

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progressDialog.dismiss();
			}

		});
		//webView.loadUrl("http://www.kraveapp.net");
		
		
		
		
		
		
		
		
		
		
		webView.loadDataWithBaseURL("", webData, "text/html", "UTF-8", "");

	}

	private void setTypeFace() {

		Typeface typeface = FontStyle.getFont(Activity_TermAndCondition.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		titel.setTypeface(typeface);

	}

	private void setLayout() {
		titel = (TextView) findViewById(R.id.titleTextView);
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setVisibility(View.VISIBLE);
		webView = (WebView) findViewById(R.id.webView1);
		titel.setText("TERM AND CONDITIONS");

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}

}