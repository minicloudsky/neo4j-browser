package org.neo4j.impl.persistence;

// JTA imports
import java.util.logging.Logger;

/**
 * The persistence layer component responsible for demarcating the
 * persistent entity space and dispatching persistent state to
 * designated persistence sources.
 * <P>
 * It's the PersistenceSourceDispatcher's job to ensure that, for
 * example, node <CODE>42</CODE> is persisted in persistence source
 * <CODE>A</CODE> while node <CODE>43</CODE> is persisted in persistence
 * source <CODE>B</CODE>.
 * <P>
 * The present implementation, however, only supports one persistence source.
 * <P>
 * Normally, the only component that interacts with the
 * PersistenceSourceDispatcher is the {@link ResourceBroker}.
 */
class PersistenceSourceDispatcher
{
	private static Logger log = Logger.getLogger(
		PersistenceSourceDispatcher.class.getName() );

	private PersistenceSource ourOnlyPersistenceSource = null;
	
	
	private static final PersistenceSourceDispatcher instance =
		new PersistenceSourceDispatcher();
	
	private PersistenceSourceDispatcher() { }
	
	/**
	 * Singleton accessor.
	 * @return the singleton persistence source dispatcher
	 */
	static PersistenceSourceDispatcher getDispatcher()
	{
		return instance;
	}
	
	/**
	 * Returns the persistence source that should be used to persist
	 * <CODE>obj</CODE>.
	 * @param obj the soon-to-be-persisted entity
	 * @return the persistence source for <CODE>obj</CODE>
	 */
	PersistenceSource getPersistenceSource() // Object obj )
	{
		return this.ourOnlyPersistenceSource;
	}
	
	/**
	 * Adds a persistence source to the dispatcher's internal
	 * list of available persistence sources. If the dispatcher
	 * already knows about <CODE>source</CODE>, this method fails
	 * silently with a message in the logs.
	 * @param source the new persistence source
	 */
	void persistenceSourceAdded( PersistenceSource source )
	{
		this.ourOnlyPersistenceSource = source;
	}
	
	/**
	 * Removes a persistence source from the dispatcher's internal
	 * list of available persistence sources. If the dispatcher
	 * is unaware of <CODE>source</CODE>, this method fails
	 * silently with a message in the logs.
	 * @param source the persistence source that will be removed
	 */
	void persistenceSourceRemoved( PersistenceSource source )
	{
		if ( this.ourOnlyPersistenceSource == source )
		{
			this.ourOnlyPersistenceSource = null;
		}
		else
		{
			log.severe(	source + " was just removed, but as far as we're " +
						"concerned, it has never been added." );
		}
	}
}
